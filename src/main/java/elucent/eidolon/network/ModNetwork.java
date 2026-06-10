package elucent.eidolon.network;

import elucent.eidolon.Reference;
import elucent.eidolon.item.RavenCloakItem;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class ModNetwork {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    private ModNetwork() {
    }

    public static void init() {
        CHANNEL.registerMessage(ResearchActionPacket.Handler.class, ResearchActionPacket.class, 0, Side.SERVER);
        registerClientMessage(KnowledgeSyncPacket.Handler.class, KnowledgeSyncPacket.class, 1);
        registerUnsafeClientMessages();
        registerClientMessage(MagicKnowledgeSyncPacket.Handler.class, MagicKnowledgeSyncPacket.class, 4);
        registerClientMessage(SoulSyncPacket.Handler.class, SoulSyncPacket.class, 5);
        CHANNEL.registerMessage(RavenCloakPacket.Handler.class, RavenCloakPacket.class, 6, Side.SERVER);
        registerClientMessage(KnowledgeResetPacket.Handler.class, KnowledgeResetPacket.class, 7);
        CHANNEL.registerMessage(AttemptCastPacket.Handler.class, AttemptCastPacket.class, 8, Side.SERVER);
        RavenCloakItem.setSyncChannel(CHANNEL);
    }

    private static <T extends IMessage> void registerClientMessage(Class<? extends IMessageHandler<T, IMessage>> handler,
                                                                   Class<T> message, int discriminator) {
        CHANNEL.registerMessage(handler, message, discriminator, Side.CLIENT);
    }

    private static void registerUnsafeClientMessages() {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            CHANNEL.registerMessage(DeathbringerSlashEffectPacket.Handler.class, DeathbringerSlashEffectPacket.class, 2, Side.CLIENT);
            CHANNEL.registerMessage(VisualEffectPacket.Handler.class, VisualEffectPacket.class, 3, Side.CLIENT);
            CHANNEL.registerMessage(SpellCastPacket.Handler.class, SpellCastPacket.class, 9, Side.CLIENT);
            CHANNEL.registerMessage(RavenCloakPacket.ClientHandler.class, RavenCloakPacket.Sync.class, 10, Side.CLIENT);
        } else {
            registerNoOpClientMessage(DeathbringerSlashEffectPacket.class, 2);
            registerNoOpClientMessage(VisualEffectPacket.class, 3);
            registerNoOpClientMessage(SpellCastPacket.class, 9);
            registerNoOpClientMessage(RavenCloakPacket.Sync.class, 10);
        }
    }

    private static <T extends IMessage> void registerNoOpClientMessage(Class<T> message, int discriminator) {
        CHANNEL.registerMessage(new NoOpClientHandler<T>(), message, discriminator, Side.CLIENT);
    }

    public static class NoOpClientHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
        @Override
        public IMessage onMessage(T message, MessageContext ctx) {
            return null;
        }
    }
}
