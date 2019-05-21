package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.Fluid.barrel.TileEntityBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketReturnBarrel implements IMessage
{
    private boolean messageValid;
    private BlockPos pos;
    private int dimension;

    public PacketReturnBarrel(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
        this.messageValid = true;
    }

    public PacketReturnBarrel(TileEntityBarrel te)
    {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketReturnBarrel()
    {
        this.messageValid = false;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        if (!this.messageValid) {
            return;
        }
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(ioe);
            return;
        }
        this.messageValid = true;
    }

    public static class Handler implements IMessageHandler<PacketReturnBarrel, PacketBarrel>
    {

        @Override
        public PacketBarrel onMessage(PacketReturnBarrel message, MessageContext ctx)
        {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
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
