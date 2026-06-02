package elucent.eidolon.compat.jei;

import elucent.eidolon.item.AthameItem;
import elucent.eidolon.registries.ModItems;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AthameHarvestWrapper implements IRecipeWrapper {
    private final AthameItem.HarvestEntry entry;

    public AthameHarvestWrapper(AthameItem.HarvestEntry entry) {
        this.entry = entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(getSources());
        inputs.add(java.util.Collections.singletonList(new ItemStack(ModItems.ATHAME)));
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, getResult());
    }

    public List<ItemStack> getSources() {
        return entry.getSources();
    }

    public ItemStack getResult() {
        return entry.getResult();
    }
}
