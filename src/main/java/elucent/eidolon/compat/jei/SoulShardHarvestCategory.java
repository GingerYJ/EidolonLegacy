package elucent.eidolon.compat.jei;

import elucent.eidolon.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class SoulShardHarvestCategory implements IRecipeCategory<SoulShardHarvestWrapper> {
    public static final String UID = Reference.MOD_ID + ".soul_shard_harvest";

    private final IDrawable background;

    public SoulShardHarvestCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(140, 84);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("gui.eidolon.soul_shard_harvest.title");
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
    public void setRecipe(IRecipeLayout recipeLayout, SoulShardHarvestWrapper wrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 12, 28);
        recipeLayout.getItemStacks().set(0, wrapper.getSources());
        recipeLayout.getItemStacks().init(1, true, 62, 28);
        recipeLayout.getItemStacks().set(1, wrapper.getTool());
        recipeLayout.getItemStacks().init(2, false, 112, 28);
        recipeLayout.getItemStacks().set(2, wrapper.getResult());
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.fontRenderer.drawString(">", 42, 32, 0x5b4732);
        minecraft.fontRenderer.drawString(">", 92, 32, 0x5b4732);
        minecraft.fontRenderer.drawString(I18n.format("gui.eidolon.soul_shard_harvest.amount"), 28, 58, 0x5b4732);
        minecraft.fontRenderer.drawString(I18n.format("gui.eidolon.soul_shard_harvest.note"), 18, 70, 0x7d6b55);
    }
}
