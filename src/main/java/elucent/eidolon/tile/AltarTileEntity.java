package elucent.eidolon.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class AltarTileEntity extends TileEntity {
    private ItemStack offering = ItemStack.EMPTY;

    public boolean hasOffering() {
        return !offering.isEmpty();
    }

    public ItemStack getOffering() {
        return offering.copy();
    }

    public boolean addOffering(ItemStack stack) {
        if (stack.isEmpty() || hasOffering()) {
            return false;
        }
        offering = stack.splitStack(1);
        markDirty();
        notifyStateChanged();
        return true;
    }

    public ItemStack removeOffering() {
        if (offering.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = offering;
        offering = ItemStack.EMPTY;
        markDirty();
        notifyStateChanged();
        return stack;
    }

    public void setOffering(ItemStack stack) {
        offering = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
        if (!offering.isEmpty()) {
            offering.setCount(1);
        }
        markDirty();
        notifyStateChanged();
    }

    private void notifyStateChanged() {
        if (world != null && !world.isRemote) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!offering.isEmpty()) {
            compound.setTag("Offering", offering.writeToNBT(new NBTTagCompound()));
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        offering = compound.hasKey("Offering", Constants.NBT.TAG_COMPOUND)
                ? new ItemStack(compound.getCompoundTag("Offering")) : ItemStack.EMPTY;
        notifyStateChanged();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        if (world != null && world.isRemote) {
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }
}
