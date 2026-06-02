package elucent.eidolon.compat.jei;

import elucent.eidolon.Reference;
import elucent.eidolon.recipes.CrucibleRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrucibleRecipeWrapper implements IRecipeWrapper {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/codex_crucible_page.png");

    private final CrucibleRecipe recipe;

    public CrucibleRecipeWrapper(CrucibleRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        List<ItemStack> stirrerStacks = getStirrerStacks();
        if (!stirrerStacks.isEmpty()) {
            inputs.add(stirrerStacks);
        }
        inputs.add(getFluidStacks());
        for (CrucibleRecipe.Step step : recipe.getSteps()) {
            for (Ingredient ingredient : step.getIngredients()) {
                List<ItemStack> stacks = stacksFor(ingredient);
                if (!stacks.isEmpty()) {
                    inputs.add(stacks);
                }
            }
        }
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    public CrucibleRecipe getRecipe() {
        return recipe;
    }

    public List<List<ItemStack>> getDisplayStacks(CrucibleRecipe.Step step) {
        List<List<ItemStack>> display = new ArrayList<>();
        for (Ingredient ingredient : step.getIngredients()) {
            List<ItemStack> stacks = copyStacks(stacksFor(ingredient));
            if (stacks.isEmpty()) {
                continue;
            }
            if (!display.isEmpty() && canMerge(display.get(display.size() - 1), stacks)) {
                for (ItemStack stack : display.get(display.size() - 1)) {
                    stack.grow(1);
                }
            } else {
                display.add(stacks);
            }
        }
        return display;
    }

    public List<ItemStack> getStirrerStacks() {
        return stacksFor(recipe.getStirrer());
    }

    public List<ItemStack> getFluidStacks() {
        if (recipe.getFluid().getFluid() == FluidRegistry.WATER) {
            return Collections.singletonList(new ItemStack(Items.WATER_BUCKET));
        }
        if (recipe.getFluid().getFluid() == FluidRegistry.LAVA) {
            return Collections.singletonList(new ItemStack(Items.LAVA_BUCKET));
        }
        return Collections.singletonList(new ItemStack(Items.BUCKET));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        List<CrucibleRecipe.Step> steps = recipe.getSteps();
        int yStart = CrucibleRecipeCategory.getStepYStart(steps.size());
        for (int i = 0; i < steps.size(); i++) {
            CrucibleRecipe.Step step = steps.get(i);
            int y = yStart + i * 20;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            minecraft.getTextureManager().bindTexture(BACKGROUND);
            Gui.drawModalRectWithCustomSizedTexture(0, y, 128, 0, 128, 20, 256, 256);
            int x = 24;
            for (List<ItemStack> stacks : getDisplayStacks(step)) {
                if (x > 104) {
                    break;
                }
                Gui.drawModalRectWithCustomSizedTexture(x, y + 1, 176, 32, 16, 17, 256, 256);
                x += 17;
            }
            for (int j = 0; j < step.getStirs() && x <= 104; j++) {
                Gui.drawModalRectWithCustomSizedTexture(x, y + 1, 192, 32, 16, 17, 256, 256);
                x += 17;
            }
            minecraft.fontRenderer.drawString((i + 1) + ".", 7, y + 7, 0x5b4732);
        }
        int resultY = yStart + steps.size() * 20 + CrucibleRecipeCategory.RESULT_GAP;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(BACKGROUND);
        Gui.drawModalRectWithCustomSizedTexture(0, resultY, 128, 64, 128, 32, 256, 256);
    }

    private List<ItemStack> stacksFor(Ingredient ingredient) {
        if (ingredient == Ingredient.EMPTY) {
            return Collections.emptyList();
        }
        return Arrays.asList(ingredient.getMatchingStacks());
    }

    private List<ItemStack> copyStacks(List<ItemStack> stacks) {
        List<ItemStack> copy = new ArrayList<>();
        for (ItemStack stack : stacks) {
            copy.add(stack.copy());
        }
        return copy;
    }

    private boolean canMerge(List<ItemStack> left, List<ItemStack> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < left.size(); i++) {
            if (!ItemStack.areItemsEqual(left.get(i), right.get(i))
                    || !ItemStack.areItemStackTagsEqual(left.get(i), right.get(i))) {
                return false;
            }
        }
        return true;
    }
}
