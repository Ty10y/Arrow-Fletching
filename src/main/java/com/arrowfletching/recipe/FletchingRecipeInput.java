package com.arrowfletching.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * The two input slots of the fletching table, presented to the recipe system.
 * Position is semantic: {@code arrow} is the base being upgraded, {@code modifier}
 * determines which upgrade is produced.
 */
public record FletchingRecipeInput(ItemStack arrow, ItemStack modifier) implements RecipeInput {
    public static final int ARROW_SLOT = 0;
    public static final int MODIFIER_SLOT = 1;

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case ARROW_SLOT -> this.arrow;
            case MODIFIER_SLOT -> this.modifier;
            default -> throw new IllegalArgumentException("No item for index " + index);
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
