package elucent.eidolon.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CrucibleRecipe {
    private final ResourceLocation id;
    private final List<Step> steps;
    private final ItemStack result;
    private final Ingredient stirrer;
    private final FluidStack fluid;

    CrucibleRecipe(ResourceLocation id, List<Step> steps, ItemStack result, Ingredient stirrer, FluidStack fluid) {
        this.id = id;
        this.steps = Collections.unmodifiableList(new ArrayList<>(steps));
        this.result = result.copy();
        this.stirrer = stirrer;
        this.fluid = fluid.copy();
    }

    public ResourceLocation getId() {
        return id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public Ingredient getStirrer() {
        return stirrer;
    }

    public FluidStack getFluid() {
        return fluid.copy();
    }

    public boolean matches(List<ProvidedStep> providedSteps, FluidStack providedFluid) {
        if (!matchesFluid(providedFluid)) {
            return false;
        }
        if (steps.size() != providedSteps.size()) {
            return false;
        }
        for (int i = 0; i < steps.size(); i++) {
            if (!steps.get(i).matches(providedSteps.get(i), stirrer)) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesPrefix(List<ProvidedStep> providedSteps, FluidStack providedFluid) {
        if (!matchesFluid(providedFluid) || providedSteps.size() > steps.size()) {
            return false;
        }
        for (int i = 0; i < providedSteps.size(); i++) {
            if (!steps.get(i).matches(providedSteps.get(i), stirrer)) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesFluid(FluidStack providedFluid) {
        return providedFluid != null && providedFluid.containsFluid(fluid);
    }

    public static Ingredient defaultStirrer() {
        return Ingredient.fromStacks(new ItemStack(Items.STICK));
    }

    public static FluidStack defaultFluid() {
        return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
    }

    public static final class Step {
        private final int stirs;
        private final List<Ingredient> ingredients;

        Step(int stirs, List<Ingredient> ingredients) {
            this.stirs = stirs;
            this.ingredients = Collections.unmodifiableList(new ArrayList<>(ingredients));
        }

        public int getStirs() {
            return stirs;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        private boolean matches(ProvidedStep provided, Ingredient stirrer) {
            if (stirs != provided.getStirs() || ingredients.size() != provided.getContents().size()) {
                return false;
            }
            if (stirs > 0 && !stirrer.apply(provided.getStirrer())) {
                return false;
            }
            List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);
            List<ItemStack> remainingStacks = new ArrayList<>(provided.getContents());
            for (int i = 0; i < remainingIngredients.size(); i++) {
                Ingredient ingredient = remainingIngredients.get(i);
                for (int j = 0; j < remainingStacks.size(); j++) {
                    if (ingredient.apply(remainingStacks.get(j))) {
                        remainingIngredients.remove(i--);
                        remainingStacks.remove(j);
                        break;
                    }
                }
            }
            return remainingIngredients.isEmpty() && remainingStacks.isEmpty();
        }
    }

    public static final class ProvidedStep {
        private final int stirs;
        private final ItemStack stirrer;
        private final List<ItemStack> contents;

        public ProvidedStep(int stirs, List<ItemStack> contents) {
            this(stirs, ItemStack.EMPTY, contents);
        }

        public ProvidedStep(int stirs, ItemStack stirrer, List<ItemStack> contents) {
            this.stirs = stirs;
            this.stirrer = stirrer.copy();
            this.contents = Collections.unmodifiableList(new ArrayList<>(contents));
        }

        public int getStirs() {
            return stirs;
        }

        public ItemStack getStirrer() {
            return stirrer.copy();
        }

        public List<ItemStack> getContents() {
            return contents;
        }
    }
}
