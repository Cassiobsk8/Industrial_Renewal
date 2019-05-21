package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.energy.batterybank.TileEntityBatteryBank;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * CLIENT SIDE
 */
public class PacketBatteryBank implements IMessage {

    private boolean messageValid;

    private BlockPos pos;
    private NBTTagCompound tag;

    public PacketBatteryBank() {
        this.messageValid = false;
    }

    public PacketBatteryBank(BlockPos pos, NBTTagCompound tag) {
        this.pos = pos;
        this.tag = tag;
        this.messageValid = true;
    }

    public PacketBatteryBank(TileEntityBatteryBank te) {
        this(te.getPos(), te.GetTag());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            tag = ByteBufUtils.readTag(buf);
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
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<PacketBatteryBank, IMessage> {

        @Override
        public IMessage onMessage(PacketBatteryBank message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketBatteryBank message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            if (world == null) {
                System.out.println("error: world is null at PacketFluidLoader");
                return;
            }
            TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(message.pos);
            if (te != null) {
                te.readTankFromNBT(message.tag);
            }
        }
    }
}
