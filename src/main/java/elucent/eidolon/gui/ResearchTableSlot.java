package elucent.eidolon.gui;

import elucent.eidolon.tile.ResearchTableTileEntity;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ResearchTableSlot extends Slot {
    private final ResearchTableTileEntity tile;

    public ResearchTableSlot(ResearchTableTileEntity tile, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.tile = tile;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return tile.isItemValidForSlot(getSlotIndex(), stack);
    }

    @Override
    public int getSlotStackLimit() {
        return getSlotIndex() == ResearchTableTileEntity.SLOT_NOTES ? 1 : super.getSlotStackLimit();
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
