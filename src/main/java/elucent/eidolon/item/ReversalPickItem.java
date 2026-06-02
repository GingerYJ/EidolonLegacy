package elucent.eidolon.item;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ReversalPickItem extends ItemPickaxe {
    public ReversalPickItem(ToolMaterial material) {
        super(material);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ReversalPickItem)) {
            return;
        }
        BlockPos pos = event.getPos();
        if (pos == null) {
            return;
        }
        World world = event.getEntityPlayer().world;
        float hardness = event.getState().getBlockHardness(world, pos);
        if (hardness <= 0.0F) {
            return;
        }
        float adjustedHardness = 1.0F / (hardness / 2.0F);
        float newSpeed = MathHelper.sqrt(event.getOriginalSpeed() * 0.25F)
                * MathHelper.sqrt(hardness / adjustedHardness);
        event.setNewSpeed(newSpeed * newSpeed);
    }

    @Override
    public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, List<String> tooltip,
                               net.minecraft.client.util.ITooltipFlag flagIn) {
        tooltip.add("");
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + TextFormatting.ITALIC
                + I18n.translateToLocal("lore.eidolon.reversal_pick"));
    }
}
