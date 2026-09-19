package com.arrowfletching.menu;

import java.util.Optional;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.recipe.FletchingRecipe;
import com.arrowfletching.recipe.FletchingRecipeInput;
import com.arrowfletching.recipe.ModRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

/**
 * Menu for the custom fletching-table arrow-upgrade station.
 *
 * Two stacked input slots (bottom = base arrow, top = modifier) and a result slot.
 * The inventory is transient: a SimpleContainer owned by this menu, cleared back to
 * the player on close (vanilla crafting-table pattern). No block entity.
 */
public class FletchingTableMenu extends AbstractContainerMenu {
    public static final int ARROW_SLOT = 0;
    public static final int MODIFIER_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 39;

    private final ContainerLevelAccess access;
    private long lastSoundTime;
    private Optional<RecipeHolder<FletchingRecipe>> selectedRecipe = Optional.empty();

    private final Container container = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            FletchingTableMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };
    private final ResultContainer resultContainer = new ResultContainer();

    /** Client-side / no-world constructor used by the menu type factory. */
    public FletchingTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public FletchingTableMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenus.FLETCHING_TABLE.get(), containerId);
        this.access = access;

        // Slot coords are the SVG slot-box origin + 1 (the 16x16 item area sits 1px
        // inside the drawn bevel). Bottom = arrows, top = modifier.
        Slot arrowSlot = new Slot(this.container, ARROW_SLOT, 34, 49) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.ARROW);
            }
        };
        // Native empty-slot ghost: a faded plain arrow, drawn only while the slot is empty
        // and ignored by all interaction (it is purely a background sprite).
        arrowSlot.setBackground(Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "slot/arrow_ghost"));
        this.addSlot(arrowSlot);
        this.addSlot(new Slot(this.container, MODIFIER_SLOT, 34, 21));
        // Result item centered in the 26x26 result box (box inner starts at 121,31; (24-16)/2 = 4).
        this.addSlot(new Slot(this.resultContainer, 0, 125, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack taken) {
                FletchingTableMenu.this.onTakeResult(player, taken);
                super.onTake(player, taken);
            }
        });

        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public void slotsChanged(Container container) {
        if (container == this.container) {
            this.setupResultSlot();
        }
    }

    private void setupResultSlot() {
        this.selectedRecipe = Optional.empty();
        this.access.execute((level, pos) -> {
            if (!(level instanceof ServerLevel serverLevel)) {
                return;
            }
            ItemStack arrow = this.container.getItem(ARROW_SLOT);
            ItemStack modifier = this.container.getItem(MODIFIER_SLOT);
            ItemStack result = ItemStack.EMPTY;
            if (!arrow.isEmpty() && !modifier.isEmpty()) {
                FletchingRecipeInput input = new FletchingRecipeInput(arrow, modifier);
                Optional<RecipeHolder<FletchingRecipe>> found =
                        serverLevel.recipeAccess().getRecipeFor(ModRecipes.FLETCHING_TYPE.get(), input, serverLevel);
                if (found.isPresent()) {
                    this.selectedRecipe = found;
                    result = found.get().value().assemble(input);
                }
            }
            this.resultContainer.setItem(0, result);
            this.resultContainer.setRecipeUsed(this.selectedRecipe.<RecipeHolder<?>>map(h -> h).orElse(null));
            this.broadcastChanges();
        });
    }

    private void onTakeResult(Player player, ItemStack taken) {
        this.selectedRecipe.ifPresent(holder -> {
            FletchingRecipe recipe = holder.value();
            this.container.removeItem(ARROW_SLOT, recipe.arrowCount());
            this.container.removeItem(MODIFIER_SLOT, recipe.modifierCount());
        });
        taken.getItem().onCraftedBy(taken, player);
        this.access.execute((level, pos) -> {
            long gameTime = level.getGameTime();
            if (this.lastSoundTime != gameTime) {
                level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.lastSoundTime = gameTime;
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack moved = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            moved = stack.copy();
            if (slotIndex == RESULT_SLOT) {
                // Result -> inventory. onTake handles input consumption.
                stack.getItem().onCraftedBy(stack, player);
                if (!this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, moved);
            } else if (slotIndex == ARROW_SLOT || slotIndex == MODIFIER_SLOT) {
                // Input -> inventory.
                if (!this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Inventory -> deliberate routing: arrows to the bottom slot, everything else to the top.
                if (stack.is(Items.ARROW)) {
                    if (!this.moveItemStackTo(stack, ARROW_SLOT, ARROW_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stack, MODIFIER_SLOT, MODIFIER_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == moved.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            this.broadcastChanges();
        }
        return moved;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot target) {
        return target.container != this.resultContainer && super.canTakeItemForPickAll(stack, target);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, Blocks.FLETCHING_TABLE);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(0);
        this.access.execute((level, pos) -> this.clearContainer(player, this.container));
    }
}
