package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.entity.EntityLogCart;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * CLIENT SIDE
 */
public class PacketLogCart implements IMessage {

    private boolean messageValid;

    private int count;
    private int id;

    public PacketLogCart() {
        this.messageValid = false;
    }

    public PacketLogCart(int invcount, int id) {
        this.count = invcount;
        this.id = id;
        this.messageValid = true;
    }

    public PacketLogCart(EntityLogCart entity) {
        this(entity.invItensCount, entity.getEntityId());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            count = buf.readInt();
            id = buf.readInt();
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(ioe);
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeInt(count);
        buf.writeInt(id);

    }

    public static class Handler implements IMessageHandler<PacketLogCart, IMessage> {

        @Override
        public IMessage onMessage(PacketLogCart message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketLogCart message, MessageContext ctx) {
            EntityLogCart entity = (EntityLogCart) Minecraft.getMinecraft().world.getEntityByID(message.id);
            if (entity != null) {
                entity.invItensCount = message.count;
            }
        }
    }
}
