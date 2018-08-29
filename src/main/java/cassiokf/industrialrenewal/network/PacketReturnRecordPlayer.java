package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.recordplayer.TileEntityRecordPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * SERVER SIDE
 */
public class PacketReturnRecordPlayer implements IMessage {

    private BlockPos pos;
    private int dimension;
    private boolean messageValid;

    public PacketReturnRecordPlayer() {
        this.messageValid = false;
    }

    public PacketReturnRecordPlayer(BlockPos pos, int dimension) {
        this.dimension = dimension;
        this.pos = pos;
        this.messageValid = true;
    }

    public PacketReturnRecordPlayer(TileEntityRecordPlayer te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
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
    }

    public static class Handler implements IMessageHandler<PacketReturnRecordPlayer, PacketRecordPlayer> {

        @Override
        public PacketRecordPlayer onMessage(PacketReturnRecordPlayer message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityRecordPlayer te = (TileEntityRecordPlayer) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketRecordPlayer(te);
            } else {
                return null;
            }
        }

    }
}
