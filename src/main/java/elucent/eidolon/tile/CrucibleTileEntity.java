package elucent.eidolon.tile;

import elucent.eidolon.recipes.CrucibleRecipe;
import elucent.eidolon.recipes.CrucibleRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class CrucibleTileEntity extends TileEntity {
    private final List<CrucibleRecipe.ProvidedStep> completedSteps = new ArrayList<>();
    private final NonNullList<ItemStack> currentContents = NonNullList.create();
    private int currentStirs;
    private ItemStack currentStirrer = ItemStack.EMPTY;
    private FluidStack fluid;
    private long lastStirTime = -100L;

    public boolean addOne(ItemStack stack) {
        if (stack.isEmpty() || !hasFluid()) {
            return false;
        }
        ItemStack stored = stack.splitStack(1);
        currentContents.add(stored);
        markDirty();
        notifyStateChanged();
        return true;
    }

    public void stir(ItemStack stirrer) {
        if (!hasFluid()) {
            return;
        }
        if (currentStirrer.isEmpty()) {
            currentStirrer = stirrer.copy();
            currentStirrer.setCount(1);
        }
        currentStirs++;
        if (world != null) {
            lastStirTime = world.getTotalWorldTime();
        }
        markDirty();
        notifyStateChanged();
    }

    public ItemStack commitStep() {
        if (!hasFluid() || (currentContents.isEmpty() && currentStirs == 0)) {
            return ItemStack.EMPTY;
        }
        completedSteps.add(new CrucibleRecipe.ProvidedStep(currentStirs, currentStirrer, copyStacks(currentContents)));
        currentContents.clear();
        currentStirs = 0;
        currentStirrer = ItemStack.EMPTY;
        notifyStateChanged();

        CrucibleRecipe recipe = CrucibleRecipes.find(completedSteps, fluid);
        if (recipe != null) {
            ItemStack result = recipe.getResult();
            reset();
            return result;
        }
        if (!CrucibleRecipes.matchesAnyPrefix(completedSteps, fluid)) {
            reset();
            return ItemStack.EMPTY;
        }
        markDirty();
        return ItemStack.EMPTY;
    }

    public boolean fill(FluidStack stack) {
        if (stack == null || hasFluid() || !CrucibleRecipes.acceptsFluid(stack)) {
            return false;
        }
        fluid = stack.copy();
        markDirty();
        notifyStateChanged();
        return true;
    }

    public List<ItemStack> getDroppedStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (CrucibleRecipe.ProvidedStep step : completedSteps) {
            stacks.addAll(copyStacks(step.getContents()));
        }
        stacks.addAll(copyStacks(currentContents));
        return stacks;
    }

    public void reset() {
        completedSteps.clear();
        currentContents.clear();
        currentStirs = 0;
        currentStirrer = ItemStack.EMPTY;
        fluid = null;
        markDirty();
        notifyStateChanged();
    }

    public boolean hasContents() {
        return hasFluid() || !completedSteps.isEmpty() || !currentContents.isEmpty() || currentStirs > 0;
    }

    public boolean hasFluid() {
        return fluid != null && fluid.amount > 0;
    }

    public FluidStack getFluid() {
        return fluid == null ? null : fluid.copy();
    }

    public List<ItemStack> getCurrentContents() {
        List<ItemStack> contents = new ArrayList<>();
        for (CrucibleRecipe.ProvidedStep step : completedSteps) {
            contents.addAll(copyStacks(step.getContents()));
        }
        contents.addAll(copyStacks(currentContents));
        return contents;
    }

    public int getCompletedStepCount() {
        return completedSteps.size();
    }

    public long getLastStirTime() {
        return lastStirTime;
    }

    private void notifyStateChanged() {
        if (world != null && !world.isRemote) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    private static List<ItemStack> copyStacks(List<ItemStack> stacks) {
        List<ItemStack> copies = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                copies.add(stack.copy());
            }
        }
        return copies;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("CompletedSteps", writeSteps(completedSteps));
        compound.setTag("CurrentContents", writeStacks(currentContents));
        compound.setInteger("CurrentStirs", currentStirs);
        compound.setLong("LastStirTime", lastStirTime);
        if (!currentStirrer.isEmpty()) {
            compound.setTag("CurrentStirrer", currentStirrer.writeToNBT(new NBTTagCompound()));
        }
        if (fluid != null) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            fluid.writeToNBT(fluidTag);
            compound.setTag("Fluid", fluidTag);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        completedSteps.clear();
        NBTTagList stepTags = compound.getTagList("CompletedSteps", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < stepTags.tagCount(); i++) {
            NBTTagCompound stepTag = stepTags.getCompoundTagAt(i);
            int stirs = stepTag.getInteger("Stirs");
            ItemStack stirrer = stepTag.hasKey("Stirrer", Constants.NBT.TAG_COMPOUND)
                    ? new ItemStack(stepTag.getCompoundTag("Stirrer")) : ItemStack.EMPTY;
            completedSteps.add(new CrucibleRecipe.ProvidedStep(stirs, stirrer, readStacks(stepTag.getTagList("Contents", Constants.NBT.TAG_COMPOUND))));
        }

        currentContents.clear();
        currentContents.addAll(readStacks(compound.getTagList("CurrentContents", Constants.NBT.TAG_COMPOUND)));
        currentStirs = compound.getInteger("CurrentStirs");
        lastStirTime = compound.hasKey("LastStirTime", Constants.NBT.TAG_LONG) ? compound.getLong("LastStirTime") : -100L;
        currentStirrer = compound.hasKey("CurrentStirrer", Constants.NBT.TAG_COMPOUND)
                ? new ItemStack(compound.getCompoundTag("CurrentStirrer")) : ItemStack.EMPTY;
        fluid = compound.hasKey("Fluid", Constants.NBT.TAG_COMPOUND)
                ? FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("Fluid")) : null;
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

    private static NBTTagList writeSteps(List<CrucibleRecipe.ProvidedStep> steps) {
        NBTTagList tags = new NBTTagList();
        for (CrucibleRecipe.ProvidedStep step : steps) {
            NBTTagCompound stepTag = new NBTTagCompound();
            stepTag.setInteger("Stirs", step.getStirs());
            if (!step.getStirrer().isEmpty()) {
                stepTag.setTag("Stirrer", step.getStirrer().writeToNBT(new NBTTagCompound()));
            }
            stepTag.setTag("Contents", writeStacks(step.getContents()));
            tags.appendTag(stepTag);
        }
        return tags;
    }

    private static NBTTagList writeStacks(List<ItemStack> stacks) {
        NBTTagList tags = new NBTTagList();
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                tags.appendTag(stack.writeToNBT(new NBTTagCompound()));
            }
        }
        return tags;
    }

    private static List<ItemStack> readStacks(NBTTagList tags) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < tags.tagCount(); i++) {
            ItemStack stack = new ItemStack(tags.getCompoundTagAt(i));
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }
}
