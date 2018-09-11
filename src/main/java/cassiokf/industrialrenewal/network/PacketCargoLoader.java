package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.railroad.cargoloader.TileEntityCargoLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCargoLoader implements IMessage {

    private BlockPos pos;
    private int enumConfig;


    public PacketCargoLoader(BlockPos pos, int enumConfig) {
        this.pos = pos;
        this.enumConfig = enumConfig;
    }

    public PacketCargoLoader(TileEntityCargoLoader te) {
        this(te.getPos(), te.getWaitEnum().intValue);
    }

    public PacketCargoLoader() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(enumConfig);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        enumConfig = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketCargoLoader, IMessage> {

        @Override
        public IMessage onMessage(PacketCargoLoader message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityCargoLoader te = (TileEntityCargoLoader) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                te.setWaitEnum(message.enumConfig);
                return null;//new PacketReturnCargoLoader(te);
            });
            return null;
        }

    }
}
