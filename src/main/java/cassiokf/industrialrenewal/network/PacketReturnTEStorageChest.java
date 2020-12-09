package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.TEStorageChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
public class PacketReturnTEStorageChest implements IMessage
{
    private BlockPos pos;
    private int dimension;
    private int ButtonId;
    private int playerID;
    private boolean messageValid;

    public PacketReturnTEStorageChest()
    {
        this.messageValid = false;
    }

    public PacketReturnTEStorageChest(BlockPos pos, int dimension, int ButtonId, int playerID)
    {
        this.dimension = dimension;
        this.pos = pos;
        this.playerID = playerID;
        this.ButtonId = ButtonId;
        this.messageValid = true;
    }

    public PacketReturnTEStorageChest(TEStorageChest te, int ButtonId, int playerID)
    {
        this(te.getPos(), te.getWorld().provider.getDimension(), ButtonId, playerID);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try
        {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
            playerID = buf.readInt();
            ButtonId = buf.readInt();
        } catch (IndexOutOfBoundsException ioe)
        {
            System.out.println(ioe);
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        if (!this.messageValid) return;
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
        buf.writeInt(playerID);
        buf.writeInt(ButtonId);
    }

    public static class Handler implements IMessageHandler<PacketReturnTEStorageChest, IMessage>
    {

        @Override
        public IMessage onMessage(PacketReturnTEStorageChest message, MessageContext ctx)
        {
            if (!message.messageValid && ctx.side != Side.CLIENT) return null;
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntity te = world.getTileEntity(message.pos);
            if (te instanceof TEStorageChest)
            {
                ((TEStorageChest) te).guiButtonClick(message.ButtonId, (EntityPlayer) world.getEntityByID(message.playerID));
                return new PacketStorageChest((TEStorageChest) te);
            }
            return null;
        }
    }
}
