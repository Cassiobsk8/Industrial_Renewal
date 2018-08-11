package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.firstaidkit.TileEntityFirstAidKit;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateFirstAidKit implements IMessage {

    private BlockPos pos;
    private int dimension;

    public PacketRequestUpdateFirstAidKit(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketRequestUpdateFirstAidKit(TileEntityFirstAidKit te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketRequestUpdateFirstAidKit() {
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

    public static class Handler implements IMessageHandler<PacketRequestUpdateFirstAidKit, PacketUpdateFirstAidKit> {

        @Override
        public PacketUpdateFirstAidKit onMessage(PacketRequestUpdateFirstAidKit message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketUpdateFirstAidKit(te);
            } else {
                return null;
            }
        }

    }

}
