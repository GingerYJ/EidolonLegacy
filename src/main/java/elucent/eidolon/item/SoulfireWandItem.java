package elucent.eidolon.item;

import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.registries.ModSounds;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class SoulfireWandItem extends WandItem {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (getCharge(stack) <= 0) {
            if (!worldIn.isRemote) {
                playerIn.sendStatusMessage(new TextComponentTranslation("message.eidolon.wand_no_charge"), true);
            }
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        if (!worldIn.isRemote) {
            Vec3d look = playerIn.getLookVec();
            SoulfireProjectileEntity projectile = new SoulfireProjectileEntity(worldIn, playerIn);
            projectile.setPosition(playerIn.posX + look.x * 0.7D,
                    playerIn.posY + playerIn.getEyeHeight() - 0.15D + look.y * 0.7D,
                    playerIn.posZ + look.z * 0.7D);
            projectile.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.8F, 0.0F);
            worldIn.spawnEntity(projectile);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    ModSounds.CAST_SOULFIRE, SoundCategory.PLAYERS, 0.75F, itemRand.nextFloat() * 0.2F + 0.9F);
            consumeCharge(stack, 1);
        }
        playerIn.swingArm(handIn);
        playerIn.getCooldownTracker().setCooldown(this, 10);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("tooltip.eidolon.soulfire_wand_use"));
    }
}
