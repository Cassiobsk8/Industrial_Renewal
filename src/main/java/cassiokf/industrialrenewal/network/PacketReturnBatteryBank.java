package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.energy.batterybank.TileEntityBatteryBank;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * SERVER SIDE
 */
public class PacketReturnBatteryBank implements IMessage {

    private boolean messageValid;
    private BlockPos pos;
    private int dimension;

    public PacketReturnBatteryBank() {
        this.messageValid = false;
    }

    public PacketReturnBatteryBank(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
        this.messageValid = true;
    }

    public PacketReturnBatteryBank(TileEntityBatteryBank te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            dimension = buf.readInt();
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(ioe);
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    public static class Handler implements IMessageHandler<PacketReturnBatteryBank, PacketBatteryBank> {

        @Override
        public PacketBatteryBank onMessage(PacketReturnBatteryBank message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT) {
                return null;
            }
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketBatteryBank(te);
            } else {
                return null;
            }
        }

    }
}
