package elucent.eidolon.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class WandItem extends Item implements IRechargeableWand {
    private static final String CHARGE_TAG = "EidolonCharge";
    private static final int MAX_CHARGE = 253;

    public WandItem() {
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public int getItemEnchantability() {
        return 20;
    }

    @Override
    public ItemStack recharge(ItemStack stack) {
        ItemStack charged = stack.copy();
        setCharge(charged, MAX_CHARGE);
        charged.setItemDamage(0);
        return charged;
    }

    public int getCharge(ItemStack stack) {
        migrateDamageToCharge(stack);
        if (!stack.hasTagCompound()) {
            return MAX_CHARGE;
        }
        NBTTagCompound tag = stack.getTagCompound();
        return tag.hasKey(CHARGE_TAG) ? Math.max(0, Math.min(MAX_CHARGE, tag.getInteger(CHARGE_TAG))) : MAX_CHARGE;
    }

    public int getMaxCharge() {
        return MAX_CHARGE;
    }

    public boolean consumeCharge(ItemStack stack, int amount) {
        int charge = getCharge(stack);
        if (charge < amount) {
            return false;
        }
        setCharge(stack, charge - amount);
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.eidolon.wand_charge", getCharge(stack), getMaxCharge()));
    }

    private void setCharge(ItemStack stack, int charge) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setInteger(CHARGE_TAG, Math.max(0, Math.min(MAX_CHARGE, charge)));
    }

    private void migrateDamageToCharge(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(CHARGE_TAG)) {
            return;
        }
        if (stack.getItemDamage() > 0) {
            setCharge(stack, Math.max(0, MAX_CHARGE - stack.getItemDamage()));
            stack.setItemDamage(0);
        }
    }
}
