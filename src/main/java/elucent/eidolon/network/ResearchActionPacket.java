package elucent.eidolon.network;

import elucent.eidolon.gui.ResearchTableContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ResearchActionPacket implements IMessage {
    public static final int ACTION_SUBMIT_TASK = 0;
    public static final int ACTION_STAMP = 1;

    private int action;
    private int taskIndex;

    public ResearchActionPacket() {
    }

    public ResearchActionPacket(int taskIndex) {
        this(ACTION_SUBMIT_TASK, taskIndex);
    }

    public ResearchActionPacket(int action, int taskIndex) {
        this.action = action;
        this.taskIndex = taskIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        action = buf.readInt();
        taskIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(action);
        buf.writeInt(taskIndex);
    }

    public static class Handler implements IMessageHandler<ResearchActionPacket, IMessage> {
        @Override
        public IMessage onMessage(ResearchActionPacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (player.openContainer instanceof ResearchTableContainer) {
                    ResearchTableContainer container = (ResearchTableContainer) player.openContainer;
                    if (message.action == ACTION_STAMP) {
                        container.stampResearch();
                    } else {
                        container.submitTask(message.taskIndex);
                    }
                }
            });
            return null;
        }
    }
}
