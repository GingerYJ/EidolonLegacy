package elucent.eidolon.compat.jei;

import elucent.eidolon.Reference;
import elucent.eidolon.registries.ModItems;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class AthameHarvestCategory implements IRecipeCategory<AthameHarvestWrapper> {
    public static final String UID = Reference.MOD_ID + ".athame_harvest";

    private final IDrawable background;

    public AthameHarvestCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(128, 72);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("gui.eidolon.athame_harvest.title");
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
    public void setRecipe(IRecipeLayout recipeLayout, AthameHarvestWrapper wrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 12, 28);
        recipeLayout.getItemStacks().set(0, wrapper.getSources());
        recipeLayout.getItemStacks().init(1, true, 56, 28);
        recipeLayout.getItemStacks().set(1, new ItemStack(ModItems.ATHAME));
        recipeLayout.getItemStacks().init(2, false, 100, 28);
        recipeLayout.getItemStacks().set(2, wrapper.getResult());
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.fontRenderer.drawString("1/3", 55, 52, 0x5b4732);
        minecraft.fontRenderer.drawString(">", 38, 32, 0x5b4732);
        minecraft.fontRenderer.drawString(">", 82, 32, 0x5b4732);
    }
}
