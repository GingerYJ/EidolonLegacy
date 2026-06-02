package elucent.eidolon.network;

import elucent.eidolon.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class ModNetwork {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    private ModNetwork() {
    }

    public static void init() {
        CHANNEL.registerMessage(ResearchActionPacket.Handler.class, ResearchActionPacket.class, 0, Side.SERVER);
        CHANNEL.registerMessage(KnowledgeSyncPacket.Handler.class, KnowledgeSyncPacket.class, 1, Side.CLIENT);
        CHANNEL.registerMessage(DeathbringerSlashEffectPacket.Handler.class, DeathbringerSlashEffectPacket.class, 2, Side.CLIENT);
    }
}
