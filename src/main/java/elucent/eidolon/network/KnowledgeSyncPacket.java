package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class KnowledgeSyncPacket implements IMessage {
    private String research;
    private boolean known;

    public KnowledgeSyncPacket() {
    }

    public KnowledgeSyncPacket(ResourceLocation research, boolean known) {
        this.research = research.toString();
        this.known = known;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        research = ByteBufUtils.readUTF8String(buf);
        known = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, research);
        buf.writeBoolean(known);
    }

    public static class Handler implements IMessageHandler<KnowledgeSyncPacket, IMessage> {
        @Override
        public IMessage onMessage(KnowledgeSyncPacket message, MessageContext ctx) {
            Eidolon.proxy.syncKnownResearchClient(new ResourceLocation(message.research), message.known);
            return null;
        }
    }
}
