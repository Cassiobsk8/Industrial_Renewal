package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.firstaidkit.TileEntityFirstAidKit;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReturnFirstAidKit implements IMessage {

    private BlockPos pos;
    private int dimension;

    public PacketReturnFirstAidKit(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketReturnFirstAidKit(TileEntityFirstAidKit te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketReturnFirstAidKit() {
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

    public static class Handler implements IMessageHandler<PacketReturnFirstAidKit, PacketFirstAidKit> {

        @Override
        public PacketFirstAidKit onMessage(PacketReturnFirstAidKit message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketFirstAidKit(te);
            } else {
                return null;
            }
        }

    }

}
