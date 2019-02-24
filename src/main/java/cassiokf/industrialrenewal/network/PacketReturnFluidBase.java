package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.entity.EntityFluidBase;
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
//this run on client
public class PacketReturnFluidBase implements IMessage {

    private boolean messageValid;
    private int dimension;
    private int id;

    public PacketReturnFluidBase() {
        this.messageValid = false;
    }

    public PacketReturnFluidBase(int dimension, int id) {
        this.dimension = dimension;
        this.id = id;
        this.messageValid = true;
    }

    public PacketReturnFluidBase(EntityFluidBase entity) {
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

    //This run on server
    public static class Handler implements IMessageHandler<PacketReturnFluidBase, PacketFluidBase> {

        @Override
        public PacketFluidBase onMessage(PacketReturnFluidBase message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            EntityFluidBase entity = (EntityFluidBase) world.getEntityByID(message.id);
            if (entity != null) {
                return new PacketFluidBase(entity);
            } else {
                return null;
            }
        }

    }
}
