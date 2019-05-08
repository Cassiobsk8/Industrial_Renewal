package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.Barrel.TileEntityBarrel;
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
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketBarrel implements IMessage
{

    private BlockPos pos;
    private NBTTagCompound tag;

    public PacketBarrel(BlockPos pos, NBTTagCompound tag)
    {
        this.pos = pos;
        this.tag = tag;
    }

    public PacketBarrel(TileEntityBarrel te)
    {
        this(te.getPos(), te.GetTag());
    }

    public PacketBarrel()
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        tag = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<PacketBarrel, IMessage>
    {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketBarrel message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                World world = Minecraft.getMinecraft().world;
                if (world == null)
                {
                    System.out.println("error: world is null at PacketFluidLoader");
                    return null;
                }
                TileEntityBarrel te = (TileEntityBarrel) world.getTileEntity(message.pos);
                if (te != null)
                {
                    te.readTankFromNBT(message.tag);
                }
                return null;
            });
            return null;
        }

    }
}
