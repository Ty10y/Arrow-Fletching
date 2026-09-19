package com.arrowfletching.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

/**
 * The fletching-table upgrade recipe: a base arrow + a modifier -> upgraded arrows.
 *
 * This is a base+modifier model (like the cartography/smithing tables), not a
 * shapeless mix. Counts are data-driven fields so each recipe can tune how many
 * arrows/modifiers it consumes and how many upgraded arrows it yields. Defaults
 * are 8 arrows + 1 modifier -> 16 upgraded (the flat fletching-table default).
 */
public class FletchingRecipe implements Recipe<FletchingRecipeInput> {
    private final Ingredient arrow;
    private final int arrowCount;
    private final Ingredient modifier;
    private final int modifierCount;
    private final ItemStackTemplate result;
    private @Nullable PlacementInfo placementInfo;

    public FletchingRecipe(Ingredient arrow, int arrowCount, Ingredient modifier, int modifierCount, ItemStackTemplate result) {
        this.arrow = arrow;
        this.arrowCount = arrowCount;
        this.modifier = modifier;
        this.modifierCount = modifierCount;
        this.result = result;
    }

    /** How many base arrows this recipe consumes per craft. */
    public int arrowCount() {
        return this.arrowCount;
    }

    /** How many modifier items this recipe consumes per craft. */
    public int modifierCount() {
        return this.modifierCount;
    }

    public Ingredient arrow() {
        return this.arrow;
    }

    public Ingredient modifier() {
        return this.modifier;
    }

    @Override
    public boolean matches(FletchingRecipeInput input, Level level) {
        return this.arrow.test(input.arrow())
            && input.arrow().getCount() >= this.arrowCount
            && this.modifier.test(input.modifier())
            && input.modifier().getCount() >= this.modifierCount;
    }

    @Override
    public ItemStack assemble(FletchingRecipeInput input) {
        return this.result.create();
    }

    /** The upgraded-arrow output stack (with its count). Used for recipe display (JEI/EMI). */
    public ItemStack resultStack() {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<FletchingRecipeInput>> getSerializer() {
        return ModRecipes.FLETCHING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<FletchingRecipeInput>> getType() {
        return ModRecipes.FLETCHING_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(List.of(this.arrow, this.modifier));
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_EQUIPMENT;
    }

    public static final MapCodec<FletchingRecipe> CODEC = RecordCodecBuilder.mapCodec(
        i -> i.group(
                Ingredient.CODEC.fieldOf("arrow").forGetter(r -> r.arrow),
                Codec.INT.optionalFieldOf("arrow_count", 8).forGetter(r -> r.arrowCount),
                Ingredient.CODEC.fieldOf("modifier").forGetter(r -> r.modifier),
                Codec.INT.optionalFieldOf("modifier_count", 1).forGetter(r -> r.modifierCount),
                ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result)
            )
            .apply(i, FletchingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FletchingRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, r -> r.arrow,
        ByteBufCodecs.VAR_INT, r -> r.arrowCount,
        Ingredient.CONTENTS_STREAM_CODEC, r -> r.modifier,
        ByteBufCodecs.VAR_INT, r -> r.modifierCount,
        ItemStackTemplate.STREAM_CODEC, r -> r.result,
        FletchingRecipe::new
    );
}
