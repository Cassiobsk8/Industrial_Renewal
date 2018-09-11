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
    private int playDisk;
    private boolean play;
    private boolean messageValid;

    public PacketReturnRecordPlayer() {
        this.messageValid = false;
    }

    public PacketReturnRecordPlayer(BlockPos pos, int dimension, int playDisk, boolean play) {
        this.dimension = dimension;
        this.pos = pos;
        this.playDisk = playDisk;
        this.play = play;
        this.messageValid = true;
    }

    public PacketReturnRecordPlayer(TileEntityRecordPlayer te) {
        this(te.getPos(), te.getWorld().provider.getDimension(), 0, false);
    }

    public PacketReturnRecordPlayer(TileEntityRecordPlayer te, int playDisk) {
        this(te.getPos(), te.getWorld().provider.getDimension(), playDisk, true);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
            playDisk = buf.readInt();
            play = buf.readBoolean();
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
        buf.writeInt(playDisk);
        buf.writeBoolean(play);
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
                if (message.play) {
                    if (message.playDisk <= 3) {
                        te.playDisk(message.playDisk);
                    } else if (message.playDisk == 5) {
                        te.stop();
                    }
                }
                return new PacketRecordPlayer(te);
            } else {
                return null;
            }
        }

    }
}
