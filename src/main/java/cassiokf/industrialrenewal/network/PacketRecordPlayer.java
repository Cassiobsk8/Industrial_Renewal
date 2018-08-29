package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.recordplayer.TileEntityRecordPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * CLIENT SIDE
 */
public class PacketRecordPlayer implements IMessage {

    private boolean messageValid;
    private BlockPos pos;
    private ItemStack stack;
    private ItemStack stack1;
    private ItemStack stack2;
    private ItemStack stack3;

    public PacketRecordPlayer() {
        this.messageValid = false;
    }

    public PacketRecordPlayer(BlockPos pos, ItemStack stack, ItemStack stack1, ItemStack stack2, ItemStack stack3) {
        this.stack = stack;
        this.stack1 = stack1;
        this.stack2 = stack2;
        this.stack3 = stack3;
        this.pos = pos;
        this.messageValid = true;
    }

    public PacketRecordPlayer(TileEntityRecordPlayer te) {
        this(te.getPos(), te.inventory.getStackInSlot(0), te.inventory.getStackInSlot(1), te.inventory.getStackInSlot(2), te.inventory.getStackInSlot(3));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            stack = ByteBufUtils.readItemStack(buf);
            stack1 = ByteBufUtils.readItemStack(buf);
            stack2 = ByteBufUtils.readItemStack(buf);
            stack3 = ByteBufUtils.readItemStack(buf);
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
        ByteBufUtils.writeItemStack(buf, stack);
        ByteBufUtils.writeItemStack(buf, stack1);
        ByteBufUtils.writeItemStack(buf, stack2);
        ByteBufUtils.writeItemStack(buf, stack3);

    }

    public static class Handler implements IMessageHandler<PacketRecordPlayer, IMessage> {

        @Override
        public IMessage onMessage(PacketRecordPlayer message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketRecordPlayer message, MessageContext ctx) {
            TileEntityRecordPlayer te = (TileEntityRecordPlayer) Minecraft.getMinecraft().world.getTileEntity(message.pos);
            te.inventory.setStackInSlot(0, message.stack);
            te.inventory.setStackInSlot(1, message.stack1);
            te.inventory.setStackInSlot(2, message.stack2);
            te.inventory.setStackInSlot(3, message.stack3);
        }
    }
}
