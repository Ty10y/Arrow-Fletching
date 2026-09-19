package com.arrowfletching.recipe;

import java.util.function.Supplier;

import com.arrowfletching.ArrowFletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the fletching-table recipe type and serializer. */
public final class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ArrowFletching.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ArrowFletching.MOD_ID);

    public static final Supplier<RecipeType<FletchingRecipe>> FLETCHING_TYPE =
            RECIPE_TYPES.register("fletching",
                    () -> RecipeType.simple(Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "fletching")));

    public static final Supplier<RecipeSerializer<FletchingRecipe>> FLETCHING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fletching",
                    () -> new RecipeSerializer<>(FletchingRecipe.CODEC, FletchingRecipe.STREAM_CODEC));

    private ModRecipes() {
    }
}
