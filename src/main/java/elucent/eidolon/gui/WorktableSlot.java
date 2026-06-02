package elucent.eidolon.gui;

import elucent.eidolon.tile.WorktableTileEntity;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WorktableSlot extends Slot {
    private final WorktableTileEntity tile;

    public WorktableSlot(WorktableTileEntity tile, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.tile = tile;
    }

    @Override
    public void putStack(ItemStack stack) {
        super.putStack(stack);
        tile.markDirty();
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        tile.markDirty();
    }
}
