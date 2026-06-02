package elucent.eidolon.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public final class WorktableRecipe {
    public static final int GRID_SIZE = 9;
    public static final int REAGENT_SIZE = 4;

    private final ResourceLocation id;
    private final Ingredient[] grid;
    private final Ingredient[] reagents;
    private final ItemStack result;

    WorktableRecipe(ResourceLocation id, Ingredient[] grid, Ingredient[] reagents, ItemStack result) {
        if (grid.length != GRID_SIZE) {
            throw new IllegalArgumentException("Worktable grid must have 9 ingredients");
        }
        if (reagents.length != REAGENT_SIZE) {
            throw new IllegalArgumentException("Worktable reagents must have 4 ingredients");
        }
        this.id = id;
        this.grid = Arrays.copyOf(grid, grid.length);
        this.reagents = Arrays.copyOf(reagents, reagents.length);
        this.result = result.copy();
    }

    public ResourceLocation getId() {
        return id;
    }

    public Ingredient getGridIngredient(int index) {
        return grid[index];
    }

    public Ingredient getReagent(int index) {
        return reagents[index];
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public boolean matches(ItemStack[] inputGrid, ItemStack[] inputReagents) {
        if (inputGrid.length != GRID_SIZE || inputReagents.length != REAGENT_SIZE) {
            return false;
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            if (!matches(grid[i], inputGrid[i])) {
                return false;
            }
        }
        for (int i = 0; i < REAGENT_SIZE; i++) {
            if (!matches(reagents[i], inputReagents[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(Ingredient ingredient, ItemStack stack) {
        if (ingredient == Ingredient.EMPTY) {
            return stack.isEmpty();
        }
        return !stack.isEmpty() && ingredient.apply(stack);
    }
}
