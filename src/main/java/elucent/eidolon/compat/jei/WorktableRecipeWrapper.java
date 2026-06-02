package elucent.eidolon.compat.jei;

import elucent.eidolon.recipes.WorktableRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorktableRecipeWrapper implements IRecipeWrapper {
    private final WorktableRecipe recipe;

    public WorktableRecipeWrapper(WorktableRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        for (int i = 0; i < WorktableRecipe.GRID_SIZE; i++) {
            addIfPresent(inputs, recipe.getGridIngredient(i));
        }
        for (int i = 0; i < WorktableRecipe.REAGENT_SIZE; i++) {
            addIfPresent(inputs, recipe.getReagent(i));
        }
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    public List<ItemStack> getGridStacks(int index) {
        return stacksFor(recipe.getGridIngredient(index));
    }

    public List<ItemStack> getReagentStacks(int index) {
        return stacksFor(recipe.getReagent(index));
    }

    public ItemStack getResult() {
        return recipe.getResult();
    }

    private void addIfPresent(List<List<ItemStack>> inputs, Ingredient ingredient) {
        List<ItemStack> stacks = stacksFor(ingredient);
        if (!stacks.isEmpty()) {
            inputs.add(stacks);
        }
    }

    private List<ItemStack> stacksFor(Ingredient ingredient) {
        if (ingredient == Ingredient.EMPTY) {
            return Collections.emptyList();
        }
        return Arrays.asList(ingredient.getMatchingStacks());
    }
}
