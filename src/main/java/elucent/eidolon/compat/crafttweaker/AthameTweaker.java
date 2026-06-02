package elucent.eidolon.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import elucent.eidolon.item.AthameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenRegister
@ZenClass("mods.eidolon.Athame")
public final class AthameTweaker {
    private AthameTweaker() {
    }

    @ZenMethod
    public static void addRecipe(String id, IIngredient source, IItemStack output, @Optional String sourceLabel) {
        ResourceLocation recipeId = TweakerUtil.id(id);
        Ingredient convertedSource = TweakerUtil.ingredient(source);
        List<ItemStack> sourceExamples = TweakerUtil.examples(source);
        ItemStack result = TweakerUtil.stack(output);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon Athame harvest recipe " + recipeId) {
            @Override
            public void apply() {
                AthameItem.addHarvestEntry(recipeId, convertedSource, sourceExamples, result, sourceLabel);
            }
        });
    }

    @ZenMethod
    public static void removeById(String id) {
        ResourceLocation recipeId = TweakerUtil.id(id);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon Athame harvest recipe " + recipeId) {
            @Override
            public void apply() {
                AthameItem.removeHarvestEntry(recipeId);
            }
        });
    }

    @ZenMethod
    public static void removeByOutput(IIngredient output) {
        Ingredient ingredient = TweakerUtil.ingredient(output);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon Athame harvest recipes with output " + output.toCommandString()) {
            @Override
            public void apply() {
                AthameItem.removeHarvestEntriesByOutput(ingredient);
            }
        });
    }

    @ZenMethod
    public static void removeBySource(IIngredient source) {
        Ingredient ingredient = TweakerUtil.ingredient(source);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon Athame harvest recipes with source " + source.toCommandString()) {
            @Override
            public void apply() {
                AthameItem.removeHarvestEntriesBySource(ingredient);
            }
        });
    }

    @ZenMethod
    public static void removeAll() {
        CraftTweakerAPI.apply(new NamedAction("Removing all Eidolon Athame harvest recipes") {
            @Override
            public void apply() {
                AthameItem.removeAllHarvestEntries();
            }
        });
    }
}
