package elucent.eidolon.tile;

import elucent.eidolon.research.Researches;
import elucent.eidolon.registries.ModItems;
import elucent.eidolon.research.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ResearchTableTileEntity extends TileEntity implements IInventory, ITickable {
    public static final int SLOT_NOTES = 0;
    public static final int SLOT_SEAL = 1;
    public static final int SLOT_COUNT = 2;
    public static final int RESEARCH_PROGRESS_TICKS = 200;

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int progress;

    @Override
    public void update() {
        if (world == null || world.isRemote || progress <= 0) {
            return;
        }
        ItemStack notes = inventory.get(SLOT_NOTES);
        if (notes.isEmpty() || notes.getItem() != ModItems.RESEARCH_NOTES || !notes.hasTagCompound()) {
            progress = 0;
            markDirty();
            return;
        }

        progress--;
        if (progress == 0) {
            NBTTagCompound tag = notes.getTagCompound();
            Research research = Researches.find(new ResourceLocation(tag.getString("research")));
            if (research != null) {
                int stepsDone = tag.getInteger("stepsDone");
                if (stepsDone < research.getStars()) {
                    tag.setInteger("stepsDone", stepsDone + 1);
                    notes.setTagCompound(tag);
                    inventory.set(SLOT_NOTES, notes);
                }
            }
        }
        markDirty();
    }

    public void startResearchProgress() {
        progress = RESEARCH_PROGRESS_TICKS;
        markDirty();
    }

    public boolean isResearchInProgress() {
        return progress > 0;
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(inventory, index, count);
        if (!stack.isEmpty()) {
            markDirty();
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = ItemStackHelper.getAndRemove(inventory, index);
        if (!stack.isEmpty()) {
            markDirty();
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == SLOT_NOTES) {
            Researches.ensureDefaultResearch(stack);
            if (stack.isEmpty() || stack.getItem() != ModItems.RESEARCH_NOTES) {
                progress = 0;
            }
        }
        inventory.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "container.eidolon.research_table";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world != null && world.getTileEntity(pos) == this
                && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (index == SLOT_NOTES) {
            return stack.getItem() == ModItems.RESEARCH_NOTES || stack.getItem() == ModItems.COMPLETED_RESEARCH;
        }
        if (index == SLOT_SEAL) {
            return stack.getItem() == ModItems.ARCANE_SEAL;
        }
        return false;
    }

    @Override
    public int getField(int id) {
        return id == 0 ? progress : 0;
    }

    @Override
    public void setField(int id, int value) {
        if (id == 0) {
            progress = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, inventory);
        compound.setInteger("progress", progress);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
        ItemStackHelper.loadAllItems(compound, inventory);
        progress = compound.getInteger("progress");
    }
}
