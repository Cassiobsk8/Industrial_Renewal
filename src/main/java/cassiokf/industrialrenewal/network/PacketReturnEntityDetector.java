package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.sensors.entitydetector.TileEntityEntityDetector;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/*
 * SERVER SIDE
 */
public class PacketReturnEntityDetector implements IMessage {

    private BlockPos pos;
    private int dimension;
    private int ButtonId;
    private boolean messageValid;

    public PacketReturnEntityDetector() {
        this.messageValid = false;
    }

    public PacketReturnEntityDetector(BlockPos pos, int dimension, int ButtonId) {
        this.dimension = dimension;
        this.pos = pos;
        this.ButtonId = ButtonId;
        this.messageValid = true;
    }

    public PacketReturnEntityDetector(TileEntityEntityDetector te, int ButtonId) {
        this(te.getPos(), te.getWorld().provider.getDimension(), ButtonId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
            ButtonId = buf.readInt();
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
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
        buf.writeInt(ButtonId);
    }

    public static class Handler implements IMessageHandler<PacketReturnEntityDetector, IMessage> {

        @Override
        public IMessage onMessage(PacketReturnEntityDetector message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityEntityDetector te = (TileEntityEntityDetector) world.getTileEntity(message.pos);
            if (te != null) {
                if (message.ButtonId == 1) {
                    te.setNextDistance();
                } else if (message.ButtonId == 2) {
                    te.setNextEntityEnum(true);
                }

                return null;
            } else {
                return null;
            }
        }

    }
}
