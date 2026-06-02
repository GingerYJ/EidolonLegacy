package elucent.eidolon.compat.jei;

import elucent.eidolon.Reference;
import elucent.eidolon.recipes.CrucibleRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CrucibleRecipeCategory implements IRecipeCategory<CrucibleRecipeWrapper> {
    public static final String UID = Reference.MOD_ID + ".crucible";
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/codex_crucible_page.png");
    static final int PAGE_WIDTH = 128;
    static final int PAGE_HEIGHT = 160;
    static final int RESULT_GAP = 8;
    static final int FLUID_GAP = 24;

    private final IDrawable background;

    public CrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(BACKGROUND, 0, 0, PAGE_WIDTH, PAGE_HEIGHT);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("tile.eidolon.crucible.name");
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrucibleRecipeWrapper recipeWrapper, IIngredients ingredients) {
        int slot = 0;
        List<CrucibleRecipe.Step> steps = recipeWrapper.getRecipe().getSteps();
        int yStart = getStepYStart(steps.size());
        recipeLayout.getItemStacks().init(slot, true, 24, yStart - FLUID_GAP);
        setIfPresent(recipeLayout, slot, recipeWrapper.getFluidStacks());
        slot++;
        for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
            CrucibleRecipe.Step step = steps.get(stepIndex);
            int x = 24;
            int y = yStart + stepIndex * 20;
            for (List<ItemStack> stacks : recipeWrapper.getDisplayStacks(step)) {
                if (x > 104) {
                    break;
                }
                recipeLayout.getItemStacks().init(slot, true, x, y + 1);
                setIfPresent(recipeLayout, slot, stacks);
                x += 17;
                slot++;
            }
            for (int i = 0; i < step.getStirs() && x <= 104; i++) {
                recipeLayout.getItemStacks().init(slot, true, x, y + 1);
                setIfPresent(recipeLayout, slot, recipeWrapper.getStirrerStacks());
                x += 17;
                slot++;
            }
        }
        int resultY = yStart + steps.size() * 20 + RESULT_GAP;
        recipeLayout.getItemStacks().init(slot, false, 56, resultY + 11);
        recipeLayout.getItemStacks().set(slot, recipeWrapper.getRecipe().getResult());
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return java.util.Collections.emptyList();
    }

    private void setIfPresent(IRecipeLayout recipeLayout, int slot, List<ItemStack> stacks) {
        if (!stacks.isEmpty()) {
            recipeLayout.getItemStacks().set(slot, stacks);
        }
    }

    static int getStepYStart(int stepCount) {
        int height = stepCount * 20 + RESULT_GAP + 32;
        return Math.max(FLUID_GAP, 80 - height / 2);
    }
}
