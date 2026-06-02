package elucent.eidolon.tile;

import elucent.eidolon.spell.IRitualItemProvider;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

public class ItemHolderTileEntity extends TileEntity implements IRitualItemProvider, ITickable {
    private static final int CONSUME_EFFECT_TICKS = 24;

    private ItemStack stack = ItemStack.EMPTY;
    private ItemStack consumedStack = ItemStack.EMPTY;
    private int consumeEffectTicks;

    public boolean hasStack() {
        return !stack.isEmpty();
    }

    public ItemStack getStack() {
        return stack.copy();
    }

    public ItemStack getRenderStack() {
        return stack.isEmpty() ? consumedStack.copy() : stack.copy();
    }

    public float getConsumeEffectProgress(float partialTicks) {
        if (consumeEffectTicks <= 0) {
            return stack.isEmpty() && !consumedStack.isEmpty() ? 1.0F : 0.0F;
        }
        return Math.min(1.0F, (CONSUME_EFFECT_TICKS - consumeEffectTicks + partialTicks) / (float) CONSUME_EFFECT_TICKS);
    }

    public boolean addStack(ItemStack input) {
        if (input.isEmpty() || hasStack()) {
            return false;
        }
        stack = input.splitStack(1);
        markDirty();
        notifyStateChanged();
        return true;
    }

    public ItemStack removeStack() {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = stack;
        stack = ItemStack.EMPTY;
        markDirty();
        notifyStateChanged();
        return removed;
    }

    public boolean activate(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (held.isEmpty() && hasStack()) {
            ItemStack removed = removeStack();
            if (!removed.isEmpty() && !player.addItemStackToInventory(removed) && world != null) {
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, removed));
            }
            return true;
        }
        return !held.isEmpty() && addStack(held);
    }

    public void dropContents() {
        if (world != null && !world.isRemote && !stack.isEmpty()) {
            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack));
            stack = ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack provide() {
        return getStack();
    }

    @Override
    public void take() {
        ItemStack removed = removeStack();
        if (!removed.isEmpty()) {
            consumedStack = removed.copy();
            consumeEffectTicks = CONSUME_EFFECT_TICKS;
            notifyStateChanged();
        }
    }

    public void playFocusEffect() {
        consumeEffectTicks = CONSUME_EFFECT_TICKS;
        notifyStateChanged();
        if (world instanceof WorldServer) {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.SPELL_WITCH,
                    pos.getX() + 0.5D, pos.getY() + 0.95D, pos.getZ() + 0.5D,
                    18, 0.18D, 0.12D, 0.18D, 0.02D);
            ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                    pos.getX() + 0.5D, pos.getY() + 0.92D, pos.getZ() + 0.5D,
                    8, 0.14D, 0.08D, 0.14D, 0.01D);
        }
    }

    @Override
    public void update() {
        if (world == null || consumeEffectTicks <= 0) {
            return;
        }
        consumeEffectTicks--;
        if (world.isRemote) {
            spawnConsumeParticles();
        } else if (consumeEffectTicks == 0) {
            consumedStack = ItemStack.EMPTY;
            notifyStateChanged();
        }
    }

    private void spawnConsumeParticles() {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.9D;
        double z = pos.getZ() + 0.5D;
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                x + (world.rand.nextDouble() - 0.5D) * 0.18D,
                y + world.rand.nextDouble() * 0.12D,
                z + (world.rand.nextDouble() - 0.5D) * 0.18D,
                0.0D, 0.025D, 0.0D);
        if (world.rand.nextInt(3) == 0) {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                    x + (world.rand.nextDouble() - 0.5D) * 0.14D,
                    y,
                    z + (world.rand.nextDouble() - 0.5D) * 0.14D,
                    0.0D, 0.02D, 0.0D);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!stack.isEmpty()) {
            compound.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));
        }
        if (!consumedStack.isEmpty()) {
            compound.setTag("ConsumedStack", consumedStack.writeToNBT(new NBTTagCompound()));
        }
        compound.setInteger("ConsumeEffectTicks", consumeEffectTicks);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stack = compound.hasKey("Stack", Constants.NBT.TAG_COMPOUND)
                ? new ItemStack(compound.getCompoundTag("Stack")) : ItemStack.EMPTY;
        consumedStack = compound.hasKey("ConsumedStack", Constants.NBT.TAG_COMPOUND)
                ? new ItemStack(compound.getCompoundTag("ConsumedStack")) : ItemStack.EMPTY;
        consumeEffectTicks = compound.getInteger("ConsumeEffectTicks");
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

    protected void notifyStateChanged() {
        if (world != null && !world.isRemote) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }
}
