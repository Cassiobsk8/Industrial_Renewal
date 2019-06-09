package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.railroad.cargoloader.TileEntityCargoLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReturnCargoLoader implements IMessage {

    private BlockPos pos;
    private int dimension;

    public PacketReturnCargoLoader(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketReturnCargoLoader(TileEntityCargoLoader te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketReturnCargoLoader() {
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

    public static class Handler implements IMessageHandler<PacketReturnCargoLoader, IMessage> {

        @Override
        public IMessage onMessage(PacketReturnCargoLoader message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityCargoLoader te = (TileEntityCargoLoader) world.getTileEntity(message.pos);
            if (te != null) {
                te.setNextWaitEnum();
                //return new PacketCargoLoader(te);
                return null;
            } else {
                return null;
            }
        }

    }

}
