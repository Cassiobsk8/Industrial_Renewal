package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.TEStorageChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketStorageChest implements IMessage {
    private boolean messageValid;
    private BlockPos pos;
    private int currentLine;
    private int additionalLine;

    public PacketStorageChest() {
        this.messageValid = false;
    }

    public PacketStorageChest(BlockPos pos, int currentLine, int additionalLine) {
        this.currentLine = currentLine;
        this.additionalLine = additionalLine;
        this.pos = pos;
        this.messageValid = true;
    }

    public PacketStorageChest(TEStorageChest te) {
        this(te.getPos(), te.currentLine, te.additionalLines);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            currentLine = buf.readInt();
            additionalLine = buf.readInt();
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
        buf.writeInt(currentLine);
        buf.writeInt(additionalLine);
    }

    public static class Handler implements IMessageHandler<PacketStorageChest, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketStorageChest message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketStorageChest message, MessageContext ctx) {
            TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
            if (te instanceof TEStorageChest) {
                ((TEStorageChest) te).setLineValues(message.currentLine, message.additionalLine);
            }
        }
    }
}
