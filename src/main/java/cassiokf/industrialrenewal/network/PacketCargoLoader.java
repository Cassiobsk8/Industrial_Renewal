package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.railroad.cargoloader.TileEntityCargoLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCargoLoader implements IMessage {

    private BlockPos pos;
    private int dimension;
    private int enumConfig;


    public PacketCargoLoader(BlockPos pos, int dimension, int enumConfig) {
        this.pos = pos;
        this.dimension = dimension;
        this.enumConfig = enumConfig;
    }

    public PacketCargoLoader(TileEntityCargoLoader te) {
        this(te.getPos(), te.getWorld().provider.getDimension(), te.getWaitEnum().intValue);
    }

    public PacketCargoLoader() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
        buf.writeInt(enumConfig);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
        enumConfig = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketCargoLoader, IMessage> {

        @Override
        public IMessage onMessage(PacketCargoLoader message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
                TileEntityCargoLoader te = (TileEntityCargoLoader) world.getTileEntity(message.pos);
                if (te != null) {
                    te.setWaitEnum(message.enumConfig);
                }
                return null;//new PacketReturnCargoLoader(te);
            });
            return null;
        }

    }
}
