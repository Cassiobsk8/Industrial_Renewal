package cassiokf.industrialrenewal.tileentity.firstaidkit;

import cassiokf.industrialrenewal.Registry.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketFirstAidKit;
import cassiokf.industrialrenewal.network.PacketReturnFirstAidKit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityFirstAidKit extends TileEntity implements ICapabilityProvider {

    public ItemStackHandler inventory;

    public TileEntityFirstAidKit() {
        this.inventory = new ItemStackHandler(8) {
            @Override
            protected void onContentsChanged(int slot) {
                if (!world.isRemote) {
                    NetworkHandler.INSTANCE.sendToAllAround(new PacketFirstAidKit(TileEntityFirstAidKit.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
                }
            }
        };
    }

    public EnumFacing getFaceDirection() {

        return BlockFirstAidKit.getFaceDirection(this.world.getBlockState(this.pos));
    }

    @Override
    public void onLoad() {
        if (world.isRemote) {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnFirstAidKit(this));
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", this.inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
    }
}
