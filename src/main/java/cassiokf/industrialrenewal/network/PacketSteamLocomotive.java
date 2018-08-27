package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * CLIENT SIDE
 */
public class PacketSteamLocomotive implements IMessage {

    private boolean messageValid;

    private ItemStack stack;
    private int id;

    public PacketSteamLocomotive() {
        this.messageValid = false;
    }

    public PacketSteamLocomotive(ItemStack stack, int id) {
        this.stack = stack;
        this.id = id;
        this.messageValid = true;
    }

    public PacketSteamLocomotive(EntitySteamLocomotive entity) {
        this(entity.inventory.getStackInSlot(6), entity.getEntityId());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            stack = ByteBufUtils.readItemStack(buf);
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
        ByteBufUtils.writeItemStack(buf, stack);
        buf.writeInt(id);

    }

    public static class Handler implements IMessageHandler<PacketSteamLocomotive, IMessage> {

        @Override
        public IMessage onMessage(PacketSteamLocomotive message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketSteamLocomotive message, MessageContext ctx) {
            EntitySteamLocomotive entity = (EntitySteamLocomotive) Minecraft.getMinecraft().world.getEntityByID(message.id);
            entity.inventory.setStackInSlot(6, message.stack);
        }
    }
}
