package elucent.eidolon.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import elucent.eidolon.recipes.WorktableRecipe;
import elucent.eidolon.recipes.WorktableRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.eidolon.Worktable")
public final class WorktableTweaker {
    private WorktableTweaker() {
    }

    @ZenMethod
    public static void addRecipe(String id, IItemStack output, IIngredient[] grid, IIngredient[] reagents) {
        ResourceLocation recipeId = TweakerUtil.id(id);
        ItemStack result = TweakerUtil.stack(output);
        Ingredient[] convertedGrid = TweakerUtil.ingredients(grid, WorktableRecipe.GRID_SIZE, "Worktable grid");
        Ingredient[] convertedReagents = TweakerUtil.ingredients(reagents, WorktableRecipe.REAGENT_SIZE, "Worktable reagents");
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon Worktable recipe " + recipeId) {
            @Override
            public void apply() {
                WorktableRecipes.addRecipe(recipeId, convertedGrid, convertedReagents, result);
            }
        });
    }

    @ZenMethod
    public static void removeById(String id) {
        ResourceLocation recipeId = TweakerUtil.id(id);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon Worktable recipe " + recipeId) {
            @Override
            public void apply() {
                WorktableRecipes.removeRecipe(recipeId);
            }
        });
    }

    @ZenMethod
    public static void removeByOutput(IIngredient output) {
        Ingredient ingredient = TweakerUtil.ingredient(output);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon Worktable recipes with output " + output.toCommandString()) {
            @Override
            public void apply() {
                WorktableRecipes.removeRecipesByOutput(ingredient);
            }
        });
    }

    @ZenMethod
    public static void removeAll() {
        CraftTweakerAPI.apply(new NamedAction("Removing all Eidolon Worktable recipes") {
            @Override
            public void apply() {
                WorktableRecipes.removeAllRecipes();
            }
        });
    }
}
