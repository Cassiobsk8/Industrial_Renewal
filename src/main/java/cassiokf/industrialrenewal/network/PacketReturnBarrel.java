package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.barrel.TileEntityBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReturnBarrel implements IMessage
{

    private BlockPos pos;
    private int dimension;

    public PacketReturnBarrel(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketReturnBarrel(TileEntityBarrel te)
    {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketReturnBarrel()
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketReturnBarrel, PacketBarrel>
    {

        @Override
        public PacketBarrel onMessage(PacketReturnBarrel message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityBarrel te = (TileEntityBarrel) world.getTileEntity(message.pos);
            if (te != null)
            {
                return new PacketBarrel(te);
            } else
            {
                return null;
            }
        }

    }

}
