package crafttweaker.api.minecraft;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

/**
 * Compile-only declarations for the CraftTweaker 1.12 bridge methods used by Eidolon.
 * The runtime implementation is supplied by CraftTweaker itself.
 */
public final class CraftTweakerMC {
    private CraftTweakerMC() {
    }

    public static ItemStack getItemStack(IItemStack stack) {
        throw new UnsupportedOperationException("compile-only API");
    }

    public static ItemStack[] getExamples(IIngredient ingredient) {
        throw new UnsupportedOperationException("compile-only API");
    }

    public static FluidStack getLiquidStack(ILiquidStack stack) {
        throw new UnsupportedOperationException("compile-only API");
    }

    public static Ingredient getIngredient(IIngredient ingredient) {
        throw new UnsupportedOperationException("compile-only API");
    }
}
