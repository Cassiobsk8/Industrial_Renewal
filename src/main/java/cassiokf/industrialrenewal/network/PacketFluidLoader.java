package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.tileentity.railroad.fluidloader.TileEntityFluidLoader;
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

public class PacketFluidLoader implements IMessage {

    private BlockPos pos;
    private int enumConfig;
    private NBTTagCompound tag;


    public PacketFluidLoader(BlockPos pos, int enumConfig, NBTTagCompound tag) {
        this.pos = pos;
        this.enumConfig = enumConfig;
        this.tag = tag;
    }

    public PacketFluidLoader(TileEntityFluidLoader te) {
        this(te.getPos(), te.getWaitEnum().intValue, te.GetTag());
    }

    public PacketFluidLoader() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(enumConfig);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        enumConfig = buf.readInt();
        tag = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<PacketFluidLoader, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketFluidLoader message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;
                if (world == null) {
                    System.out.println("error: world is null at PacketFluidLoader");
                    return null;
                }
                TileEntityFluidLoader te = (TileEntityFluidLoader) world.getTileEntity(message.pos);
                if (te != null) {
                    te.setWaitEnum(message.enumConfig);
                    te.readTankFromNBT(message.tag);
                }
                return null;
            });
            return null;
        }

    }
}
