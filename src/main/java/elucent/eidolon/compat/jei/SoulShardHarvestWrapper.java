package elucent.eidolon.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SoulShardHarvestWrapper implements IRecipeWrapper {
    private final SoulShardHarvestRecipe recipe;

    public SoulShardHarvestWrapper(SoulShardHarvestRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(recipe.getSources());
        inputs.add(java.util.Collections.singletonList(recipe.getTool()));
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    public List<ItemStack> getSources() {
        return recipe.getSources();
    }

    public ItemStack getTool() {
        return recipe.getTool();
    }

    public ItemStack getResult() {
        return recipe.getResult();
    }
}
