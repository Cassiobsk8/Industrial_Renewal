package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReturnFluidLoader implements IMessage {

    private BlockPos pos;
    private int dimension;

    public PacketReturnFluidLoader(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketReturnFluidLoader(TileEntityFluidLoader te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketReturnFluidLoader() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketReturnFluidLoader, IMessage>
    {

        @Override
        public IMessage onMessage(PacketReturnFluidLoader message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityFluidLoader te = (TileEntityFluidLoader) world.getTileEntity(message.pos);
            if (te != null) {
                te.setNextWaitEnum();
                return null;
            } else {
                return null;
            }
        }

    }

}
