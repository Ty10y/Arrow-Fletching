package com.arrowfletching.compat.jei;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.recipe.FletchingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

/**
 * JEI category for the fletching-table upgrade recipes: base arrow + modifier -> upgraded arrows.
 * Laid out left-to-right: [arrow] + [modifier] -> [result], on JEI's default recipe pane.
 */
public class FletchingTableCategory implements IRecipeCategory<RecipeHolder<FletchingRecipe>> {
    public static final RecipeType<RecipeHolder<FletchingRecipe>> RECIPE_TYPE =
            RecipeType.create(ArrowFletching.MOD_ID, "fletching", asHolderClass());

    private static final int WIDTH = 100;
    private static final int HEIGHT = 26;
    private static final int SLOT_Y = 6;
    private static final int CENTER_Y = SLOT_Y + 8; // vertical middle of a 16px slot

    private final IDrawable icon;
    private final IDrawableStatic plus;
    private final IDrawableStatic arrow;

    public FletchingTableCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.FLETCHING_TABLE));
        this.plus = guiHelper.getRecipePlusSign();
        this.arrow = guiHelper.getRecipeArrow();
    }

    @SuppressWarnings("unchecked")
    private static Class<RecipeHolder<FletchingRecipe>> asHolderClass() {
        return (Class<RecipeHolder<FletchingRecipe>>) (Class<?>) RecipeHolder.class;
    }

    @Override
    public RecipeType<RecipeHolder<FletchingRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.arrow_fletching.fletching_table");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public Identifier getRegistryName(RecipeHolder<FletchingRecipe> holder) {
        return holder.id().identifier();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FletchingRecipe> holder, IFocusGroup focuses) {
        FletchingRecipe recipe = holder.value();

        builder.addSlot(RecipeIngredientRole.INPUT, 2, SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(new ItemStack(Items.ARROW, recipe.arrowCount()));

        builder.addSlot(RecipeIngredientRole.INPUT, 34, SLOT_Y)
                .setStandardSlotBackground()
                .add(recipe.modifier());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, SLOT_Y)
                .setOutputSlotBackground()
                .addItemStack(recipe.resultStack());
    }

    @Override
    public void draw(RecipeHolder<FletchingRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView slotsView,
                     GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.plus.draw(graphics, 22, CENTER_Y - this.plus.getHeight() / 2);
        this.arrow.draw(graphics, 54, CENTER_Y - this.arrow.getHeight() / 2);
    }
}
