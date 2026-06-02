package elucent.eidolon.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ResearchTaskSlot extends Slot {
    private ItemStack expected = ItemStack.EMPTY;
    private boolean active;

    public ResearchTaskSlot(InventoryBasic inventory, int index) {
        super(inventory, index, -1000, -1000);
    }

    public void configure(ItemStack expected, int x, int y) {
        this.expected = expected.copy();
        this.xPos = x;
        this.yPos = y;
        this.active = !expected.isEmpty();
    }

    public void clear(EntityPlayer player) {
        ItemStack stack = getStack();
        if (!stack.isEmpty() && !player.world.isRemote) {
            player.dropItem(stack, false);
        }
        clearVisual();
    }

    public void clearVisual() {
        putStack(ItemStack.EMPTY);
        onSlotChanged();
        expected = ItemStack.EMPTY;
        xPos = -1000;
        yPos = -1000;
        active = false;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return active
                && !stack.isEmpty()
                && stack.getItem() == expected.getItem()
                && stack.getMetadata() == expected.getMetadata()
                && (!expected.hasTagCompound() || ItemStack.areItemStackTagsEqual(stack, expected));
    }

    @Override
    public int getSlotStackLimit() {
        return active ? Math.max(1, expected.getCount()) : 0;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return active || !getStack().isEmpty();
    }

    public boolean matchesExpectation() {
        ItemStack stack = getStack();
        return stack.isEmpty() || isItemValid(stack);
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
