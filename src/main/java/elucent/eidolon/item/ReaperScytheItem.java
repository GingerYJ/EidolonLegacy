package elucent.eidolon.item;

import elucent.eidolon.registries.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ReaperScytheItem extends ItemSword {
    public ReaperScytheItem(ToolMaterial material) {
        super(material);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDrops(LivingDropsEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        if (!isReapable(target)) {
            return;
        }
        DamageSource source = event.getSource();
        if (source == null || !(source.getTrueSource() instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
        ItemStack weapon = attacker.getHeldItemMainhand();
        boolean deathbringer = weapon.getItem() instanceof DeathbringerScytheItem;
        if (!(weapon.getItem() instanceof ReaperScytheItem) && !deathbringer) {
            return;
        }
        if (target.world.isRemote) {
            return;
        }
        int count = 1 + target.world.rand.nextInt(1 + Math.max(0, event.getLootingLevel()));
        if (deathbringer) {
            count *= 3;
        }
        EntityItem drop = new EntityItem(target.world, target.posX, target.posY, target.posZ,
                new ItemStack(ModItems.SOUL_SHARD, count));
        drop.setDefaultPickupDelay();
        event.getDrops().add(drop);
    }

    private boolean isReapable(EntityLivingBase target) {
        return target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD
                || DeathbringerScytheItem.hasUndeath(target);
    }

    @Override
    public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, List<String> tooltip,
                               net.minecraft.client.util.ITooltipFlag flagIn) {
        tooltip.add("");
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + TextFormatting.ITALIC
                + I18n.translateToLocal("lore.eidolon.reaper_scythe"));
    }
}
