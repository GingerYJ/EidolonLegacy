package elucent.eidolon.gui;

import elucent.eidolon.recipes.WorktableRecipe;
import elucent.eidolon.recipes.WorktableRecipes;
import elucent.eidolon.tile.WorktableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WorktableResultSlot extends Slot {
    private final WorktableTileEntity tile;

    public WorktableResultSlot(WorktableTileEntity tile, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.tile = tile;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getStack() {
        WorktableRecipe recipe = WorktableRecipes.findMatch(tile.getGridStacks(), tile.getReagentStacks());
        return recipe == null ? ItemStack.EMPTY : recipe.getResult();
    }

    @Override
    public boolean getHasStack() {
        return !getStack().isEmpty();
    }

    @Override
    public void putStack(ItemStack stack) {
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return getStack();
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        tile.consumeInputs();
        return super.onTake(thePlayer, stack);
    }
}
