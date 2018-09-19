package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.fusebox.TileEntityFuseBox;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
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
public class PacketReturnFuseBox implements IMessage {

    private BlockPos pos;
    private int dimension;
    private int playerID;
    private boolean messageValid;

    public PacketReturnFuseBox() {
        this.messageValid = false;
    }

    public PacketReturnFuseBox(BlockPos pos, int dimension, int playerID) {
        this.dimension = dimension;
        this.pos = pos;
        this.playerID = playerID;
        this.messageValid = true;
    }

    public PacketReturnFuseBox(TileEntityFuseBox te) {
        this(te.getPos(), te.getWorld().provider.getDimension(), -1);
    }

    public PacketReturnFuseBox(TileEntityFuseBox te, int playerID) {
        this(te.getPos(), te.getWorld().provider.getDimension(), playerID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
            playerID = buf.readInt();
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
        buf.writeInt(playerID);
    }

    public static class Handler implements IMessageHandler<PacketReturnFuseBox, IMessage> {

        @Override
        public IMessage onMessage(PacketReturnFuseBox message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityFuseBox te = (TileEntityFuseBox) world.getTileEntity(message.pos);
            if (te != null) {
                if (message.playerID != -1) {
                    EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
                    te.shockPlayer(player);
                }
                return null;
            } else {
                return null;
            }
        }

    }
}
