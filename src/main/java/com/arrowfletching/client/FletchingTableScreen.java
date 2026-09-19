package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.menu.FletchingTableMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Screen for the fletching-table arrow-upgrade menu. Draws the original bevel texture
 * plus a "count hint" while the arrow slot lacks a full batch. The arrow ghost itself is
 * a native empty-slot background sprite set on the menu's arrow slot.
 */
public class FletchingTableScreen extends AbstractContainerScreen<FletchingTableMenu> {
    private static final Identifier BG =
            Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/gui/fletching_table.png");
    /** Arrows consumed per craft; matches the recipe default. Purely a display hint. */
    private static final int ARROWS_PER_CRAFT = 8;
    private static final int LABEL_COLOR = 0xFF404040;

    public FletchingTableScreen(FletchingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG, this.leftPos, this.topPos,
                0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        // Count hint: show "x8" beside the arrow slot until a full batch is present.
        ItemStack arrows = this.menu.getSlot(FletchingTableMenu.ARROW_SLOT).getItem();
        if (arrows.getCount() < ARROWS_PER_CRAFT) {
            graphics.text(this.font, Component.literal("×" + ARROWS_PER_CRAFT), 54, 53, LABEL_COLOR, false);
        }
    }
}
