package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.railroad.fluidloader.TileEntityFluidLoader;
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
    private boolean next;

    public PacketReturnFluidLoader(BlockPos pos, int dimension, boolean next) {
        this.pos = pos;
        this.dimension = dimension;
        this.next = next;
    }

    public PacketReturnFluidLoader(TileEntityFluidLoader te) {
        this(te.getPos(), te.getWorld().provider.getDimension(), false);
    }

    public PacketReturnFluidLoader(TileEntityFluidLoader te, Boolean value) {
        this(te.getPos(), te.getWorld().provider.getDimension(), value);
    }

    public PacketReturnFluidLoader() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
        buf.writeBoolean(next);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
        next = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketReturnFluidLoader, PacketFluidLoader> {

        @Override
        public PacketFluidLoader onMessage(PacketReturnFluidLoader message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityFluidLoader te = (TileEntityFluidLoader) world.getTileEntity(message.pos);
            if (te != null) {
                te.setNextWaitEnum(message.next);
                return new PacketFluidLoader(te);
            } else {
                return null;
            }
        }

    }

}
