package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * SERVER SIDE
 */
public class PacketReturnSteamLocomotive implements IMessage {

    private boolean messageValid;
    private int dimension;
    private int id;

    public PacketReturnSteamLocomotive() {
        this.messageValid = false;
    }

    public PacketReturnSteamLocomotive(int dimension, int id) {
        this.dimension = dimension;
        this.id = id;
        this.messageValid = true;
    }

    public PacketReturnSteamLocomotive(EntitySteamLocomotive entity) {
        this(entity.getEntityWorld().provider.getDimension(), entity.getEntityId());
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            dimension = buf.readInt();
            id = buf.readInt();
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(ioe);
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeInt(dimension);
        buf.writeInt(id);
    }

    public static class Handler implements IMessageHandler<PacketReturnSteamLocomotive, PacketSteamLocomotive> {

        @Override
        public PacketSteamLocomotive onMessage(PacketReturnSteamLocomotive message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            EntitySteamLocomotive entity = (EntitySteamLocomotive) world.getEntityByID(message.id);
            if (entity != null) {
                return new PacketSteamLocomotive(entity);
            } else {
                return null;
            }
        }

    }
}
