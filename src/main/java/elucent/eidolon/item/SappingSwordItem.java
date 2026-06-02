package elucent.eidolon.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SappingSwordItem extends ItemSword {
    public SappingSwordItem(ToolMaterial material) {
        super(material);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof EntityLivingBase)) {
            return;
        }
        if (!(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof SappingSwordItem)) {
            return;
        }
        EntityLivingBase target = (EntityLivingBase) event.getTarget();
        target.hurtResistantTime = 0;
        if (!event.getEntityPlayer().world.isRemote) {
            event.getEntityPlayer().heal(2.0F);
        }
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source == null || !(source.getTrueSource() instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
        if (!(attacker.getHeldItemMainhand().getItem() instanceof SappingSwordItem)) {
            return;
        }
        event.setAmount(event.getAmount() + 2.0F);
    }
}
