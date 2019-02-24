package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.entity.EntityFluidBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * CLIENT SIDE
 */
public class PacketFluidBase implements IMessage {

    private boolean messageValid;

    private int id;
    private NBTTagCompound tag;

    public PacketFluidBase() {
        this.messageValid = false;
    }

    public PacketFluidBase(int id, NBTTagCompound tag) {
        this.id = id;
        this.tag = tag;
        this.messageValid = true;
    }

    public PacketFluidBase(EntityFluidBase entity) {
        this(entity.getEntityId(), entity.GetTag());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            id = buf.readInt();
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
        buf.writeInt(id);
        ByteBufUtils.writeTag(buf, tag);
    }


    public static class Handler implements IMessageHandler<PacketFluidBase, IMessage> {

        @Override
        public IMessage onMessage(PacketFluidBase message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER) {
                return null;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                processMessage(message, ctx);
            });
            return null;
        }

        void processMessage(PacketFluidBase message, MessageContext ctx) {
            EntityFluidBase entity = (EntityFluidBase) Minecraft.getMinecraft().world.getEntityByID(message.id);
            if (entity != null) {
                entity.readTankFromNBT(message.tag);
            }
        }
    }
}
