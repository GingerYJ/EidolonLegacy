package elucent.eidolon.research;

import elucent.eidolon.Reference;
import elucent.eidolon.network.KnowledgeSyncPacket;
import elucent.eidolon.network.ModNetwork;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class KnowledgeEvents {
    private KnowledgeEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        for (String id : KnowledgeUtil.getKnownResearchIds(player)) {
            ModNetwork.CHANNEL.sendTo(new KnowledgeSyncPacket(new ResourceLocation(id), true), player);
        }
    }
}
