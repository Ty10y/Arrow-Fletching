package com.arrowfletching.compat.jei;

import java.util.ArrayList;
import java.util.Collection;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.recipe.FletchingRecipe;
import com.arrowfletching.recipe.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

/** JEI integration: registers the fletching-table category, its recipes, and the block as catalyst. */
@JeiPlugin
public class ArrowFletchingJeiPlugin implements IModPlugin {
    private static final Identifier UID = Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "jei");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new FletchingTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Recipes live on the (integrated) server in this version; the client cache doesn't hold them.
        IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        if (server == null) {
            return;
        }
        Collection<RecipeHolder<FletchingRecipe>> recipes =
                server.getRecipeManager().recipeMap().byType(ModRecipes.FLETCHING_TYPE.get());
        registration.addRecipes(FletchingTableCategory.RECIPE_TYPE, new ArrayList<>(recipes));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.FLETCHING_TABLE), FletchingTableCategory.RECIPE_TYPE);
    }
}
