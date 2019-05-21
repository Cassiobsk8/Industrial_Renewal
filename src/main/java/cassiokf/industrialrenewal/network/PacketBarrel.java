package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.barrel.TileEntityBarrel;
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

public class PacketBarrel implements IMessage
{
    private boolean messageValid;
    private BlockPos pos;
    private NBTTagCompound tag;

    public PacketBarrel(BlockPos pos, NBTTagCompound tag)
    {
        this.pos = pos;
        this.tag = tag;
        this.messageValid = true;
    }

    public PacketBarrel(TileEntityBarrel te)
    {
        this(te.getPos(), te.GetTag());
    }

    public PacketBarrel()
    {
        this.messageValid = false;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        if (!this.messageValid) {
            return;
        }
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try {
            pos = BlockPos.fromLong(buf.readLong());
            tag = ByteBufUtils.readTag(buf);
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(ioe);
        }
        this.messageValid = true;
    }

    public static class Handler implements IMessageHandler<PacketBarrel, IMessage>
    {

        @Override
        //@SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketBarrel message, MessageContext ctx)
        {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketBarrel message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            if (world == null) {
                System.out.println("error: world is null at PacketFluidLoader");
                return;
            }
            TileEntityBarrel te = (TileEntityBarrel) world.getTileEntity(message.pos);
            if (te != null) {
                te.readTankFromNBT(message.tag);
            }
        }
    }
}
