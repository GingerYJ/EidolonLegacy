package elucent.eidolon.compat.jei;

import elucent.eidolon.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class WorktableRecipeCategory implements IRecipeCategory<WorktableRecipeWrapper> {
    public static final String UID = Reference.MOD_ID + ".worktable";
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei_worktable.png");

    private final IDrawable background;

    public WorktableRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 190, 139);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("tile.eidolon.worktable.name");
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
    public void setRecipe(IRecipeLayout recipeLayout, WorktableRecipeWrapper recipeWrapper, IIngredients ingredients) {
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                recipeLayout.getItemStacks().init(index, true, 40 + col * 18, 40 + row * 18);
                setIfPresent(recipeLayout, index, recipeWrapper.getGridStacks(index));
                index++;
            }
        }

        recipeLayout.getItemStacks().init(9, true, 58, 18);
        setIfPresent(recipeLayout, 9, recipeWrapper.getReagentStacks(0));
        recipeLayout.getItemStacks().init(10, true, 98, 58);
        setIfPresent(recipeLayout, 10, recipeWrapper.getReagentStacks(1));
        recipeLayout.getItemStacks().init(11, true, 58, 98);
        setIfPresent(recipeLayout, 11, recipeWrapper.getReagentStacks(2));
        recipeLayout.getItemStacks().init(12, true, 18, 58);
        setIfPresent(recipeLayout, 12, recipeWrapper.getReagentStacks(3));

        recipeLayout.getItemStacks().init(13, false, 163, 58);
        recipeLayout.getItemStacks().set(13, recipeWrapper.getResult());
    }

    private void setIfPresent(IRecipeLayout recipeLayout, int slot, List<ItemStack> stacks) {
        if (!stacks.isEmpty()) {
            recipeLayout.getItemStacks().set(slot, stacks);
        }
    }
}
