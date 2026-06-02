package elucent.eidolon.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class SummoningStaffItem extends Item {
    private static final String ABSORBED_UNDEAD = "AbsorbedUndead";
    private static final String ABSORBED_TYPES = "AbsorbedUndeadTypes";
    private static final String SELECTED_TYPE = "SelectedUndeadType";
    private static final String LEGACY_TYPE = "minecraft:skeleton";
    public static final String SUMMONED_TAG = "EidolonSummoned";
    public static final String OWNER_TAG = "EidolonSummoner";

    public SummoningStaffItem() {
        setMaxStackSize(1);
    }

    public ItemStack addAbsorbedUndead(ItemStack stack, int amount) {
        return addAbsorbedUndead(stack, LEGACY_TYPE, amount);
    }

    public ItemStack addAbsorbedUndead(ItemStack stack, String entityId, int amount) {
        ItemStack charged = stack.copy();
        addAbsorbedUndeadInPlace(charged, entityId, amount);
        return charged;
    }

    public ItemStack addAbsorbedUndead(ItemStack stack, Map<String, Integer> absorbed) {
        ItemStack charged = stack.copy();
        for (Map.Entry<String, Integer> entry : absorbed.entrySet()) {
            addAbsorbedUndeadInPlace(charged, entry.getKey(), entry.getValue() * 5);
        }
        return charged;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                cycleSelectedType(stack);
            }
            playerIn.swingArm(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        String selected = getSelectedType(stack);
        int absorbed = getAbsorbedUndead(stack, selected);
        if (absorbed <= 0) {
            selected = findAvailableType(stack);
            absorbed = getAbsorbedUndead(stack, selected);
        }
        if (absorbed <= 0) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        if (!worldIn.isRemote) {
            Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(selected), worldIn);
            if (entity == null) {
                return new ActionResult<>(EnumActionResult.FAIL, stack);
            }
            Vec3d look = playerIn.getLookVec();
            double x = playerIn.posX + look.x * 2.0D;
            double y = playerIn.posY + 0.2D;
            double z = playerIn.posZ + look.z * 2.0D;
            entity.setPosition(x, y, z);
            entity.getEntityData().setBoolean(SUMMONED_TAG, true);
            entity.getEntityData().setString(OWNER_TAG, playerIn.getUniqueID().toString());
            if (entity instanceof EntityLiving) {
                ((EntityLiving) entity).onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entity)), null);
            }
            worldIn.spawnEntity(entity);
            setAbsorbedUndead(stack, selected, absorbed - 1);
        }
        playerIn.swingArm(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String selected = getSelectedType(stack);
        tooltip.add(I18n.format("tooltip.eidolon.summoning_staff_selected", localizeEntity(selected)));
        tooltip.add(I18n.format("tooltip.eidolon.summoning_staff_absorbed", getAbsorbedUndead(stack)));
        for (String type : getAvailableTypes(stack)) {
            tooltip.add(I18n.format("tooltip.eidolon.summoning_staff_type", localizeEntity(type), getAbsorbedUndead(stack, type)));
        }
        tooltip.add(I18n.format("tooltip.eidolon.summoning_staff_cycle"));
        tooltip.add(I18n.format("tooltip.eidolon.summoning_staff_use"));
    }

    private int getAbsorbedUndead(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        }
        NBTTagCompound tag = stack.getTagCompound();
        NBTTagCompound types = tag.getCompoundTag(ABSORBED_TYPES);
        int total = tag.getInteger(ABSORBED_UNDEAD);
        for (String key : types.getKeySet()) {
            total += types.getInteger(key);
        }
        return total;
    }

    private int getAbsorbedUndead(ItemStack stack, String entityId) {
        if (!stack.hasTagCompound()) {
            return 0;
        }
        NBTTagCompound tag = stack.getTagCompound();
        int count = tag.getCompoundTag(ABSORBED_TYPES).getInteger(entityId);
        if (count <= 0 && LEGACY_TYPE.equals(entityId)) {
            count = tag.getInteger(ABSORBED_UNDEAD);
        }
        return count;
    }

    private void setAbsorbedUndead(ItemStack stack, String entityId, int amount) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        if (LEGACY_TYPE.equals(entityId)) {
            tag.setInteger(ABSORBED_UNDEAD, 0);
        }
        NBTTagCompound types = tag.getCompoundTag(ABSORBED_TYPES);
        types.setInteger(entityId, Math.max(0, amount));
        tag.setTag(ABSORBED_TYPES, types);
    }

    private void addAbsorbedUndeadInPlace(ItemStack stack, String entityId, int amount) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        NBTTagCompound types = tag.getCompoundTag(ABSORBED_TYPES);
        types.setInteger(entityId, Math.max(0, types.getInteger(entityId) + amount));
        tag.setTag(ABSORBED_TYPES, types);
        tag.setString(SELECTED_TYPE, entityId);
    }

    private String getSelectedType(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return LEGACY_TYPE;
        }
        NBTTagCompound tag = stack.getTagCompound();
        String selected = tag.getString(SELECTED_TYPE);
        return selected.isEmpty() ? LEGACY_TYPE : selected;
    }

    private String findAvailableType(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return LEGACY_TYPE;
        }
        NBTTagCompound tag = stack.getTagCompound();
        NBTTagCompound types = tag.getCompoundTag(ABSORBED_TYPES);
        for (String key : types.getKeySet()) {
            if (types.getInteger(key) > 0) {
                tag.setString(SELECTED_TYPE, key);
                return key;
            }
        }
        if (tag.getInteger(ABSORBED_UNDEAD) > 0) {
            tag.setString(SELECTED_TYPE, LEGACY_TYPE);
            return LEGACY_TYPE;
        }
        return getSelectedType(stack);
    }

    private void cycleSelectedType(ItemStack stack) {
        List<String> types = getAvailableTypes(stack);
        if (types.isEmpty()) {
            return;
        }
        String selected = getSelectedType(stack);
        int index = types.indexOf(selected);
        String next = types.get((index + 1) % types.size());
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setString(SELECTED_TYPE, next);
    }

    private List<String> getAvailableTypes(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return Collections.emptyList();
        }
        NBTTagCompound tag = stack.getTagCompound();
        NBTTagCompound typesTag = tag.getCompoundTag(ABSORBED_TYPES);
        List<String> types = new ArrayList<>();
        for (String key : typesTag.getKeySet()) {
            if (typesTag.getInteger(key) > 0) {
                types.add(key);
            }
        }
        if (tag.getInteger(ABSORBED_UNDEAD) > 0 && !types.contains(LEGACY_TYPE)) {
            types.add(LEGACY_TYPE);
        }
        Collections.sort(types);
        return types;
    }

    private String localizeEntity(String entityId) {
        ResourceLocation id = new ResourceLocation(entityId);
        return I18n.format("entity." + id.getNamespace() + "." + id.getPath() + ".name");
    }
}
