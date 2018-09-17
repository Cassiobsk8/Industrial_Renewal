package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.sensors.entitydetector.TileEntityEntityDetector;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * CLIENT SIDE
 */
public class PacketEntityDetector implements IMessage {

    private boolean messageValid;
    private BlockPos pos;
    private int distance;
    private int enumConfig;

    public PacketEntityDetector() {
        this.messageValid = false;
    }

    public PacketEntityDetector(BlockPos pos, int distance, int enumConfig) {
        this.distance = distance;
        this.pos = pos;
        this.enumConfig = enumConfig;
        this.messageValid = true;
    }

    public PacketEntityDetector(TileEntityEntityDetector te) {
        this(te.getPos(), te.getDistance(), te.getEntityEnum().intValue);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            distance = buf.readInt();
            enumConfig = buf.readInt();
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(ioe);
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeLong(pos.toLong());
        buf.writeInt(distance);
        buf.writeInt(enumConfig);
    }

    public static class Handler implements IMessageHandler<PacketEntityDetector, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketEntityDetector message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketEntityDetector message, MessageContext ctx) {
            TileEntityEntityDetector te = (TileEntityEntityDetector) Minecraft.getMinecraft().world.getTileEntity(message.pos);
            if (te != null) {
                te.setDistance(message.distance);
                te.setEntityEnum(message.enumConfig);
            }
        }
    }
}
