package elucent.eidolon.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

final class TweakerUtil {
    private TweakerUtil() {
    }

    static ResourceLocation id(String name) {
        return new ResourceLocation(name);
    }

    static ItemStack stack(IItemStack stack) {
        return stack == null ? ItemStack.EMPTY : CraftTweakerMC.getItemStack(stack).copy();
    }

    static Ingredient ingredient(IIngredient ingredient) {
        if (ingredient == null) {
            return Ingredient.EMPTY;
        }
        Ingredient converted = CraftTweakerMC.getIngredient(ingredient);
        return converted == null ? Ingredient.EMPTY : converted;
    }

    static Ingredient[] ingredients(IIngredient[] ingredients, int size, String label) {
        if (ingredients == null || ingredients.length != size) {
            throw new IllegalArgumentException(label + " must contain exactly " + size + " ingredients");
        }
        Ingredient[] converted = new Ingredient[size];
        for (int i = 0; i < size; i++) {
            converted[i] = ingredient(ingredients[i]);
        }
        return converted;
    }

    static List<ItemStack> examples(IIngredient ingredient) {
        List<ItemStack> examples = new ArrayList<>();
        if (ingredient == null) {
            return examples;
        }
        ItemStack[] stacks = CraftTweakerMC.getExamples(ingredient);
        if (stacks != null) {
            for (ItemStack stack : stacks) {
                if (stack != null && !stack.isEmpty()) {
                    examples.add(stack.copy());
                }
            }
        }
        return examples;
    }

    static FluidStack fluid(ILiquidStack fluid, FluidStack fallback) {
        if (fluid == null) {
            return fallback.copy();
        }
        FluidStack converted = CraftTweakerMC.getLiquidStack(fluid);
        return converted == null ? fallback.copy() : converted.copy();
    }
}
