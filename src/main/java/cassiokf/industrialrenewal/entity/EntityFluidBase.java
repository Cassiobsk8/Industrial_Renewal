package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.network.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketFluidBase;
import cassiokf.industrialrenewal.network.PacketReturnFluidBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class EntityFluidBase extends EntityMinecart implements IFluidHandler {

    public FluidTank tank = new FluidTank(32000) {
        @Override
        protected void onContentsChanged() {
            if (!world.isRemote) {
                NetworkHandler.INSTANCE.sendToAllAround(new PacketFluidBase(EntityFluidBase.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 8));
            }
        }
    };

    public EntityFluidBase(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityFluidBase(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        //super.killMinecart(source);
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.cargoContainer, 1), 0.0F);
        }
    }

    public void OpenEvent() {
        if (world.isRemote) {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnFluidBase(this));
        }
    }

    @Override
    public Type getType() {
        return Type.CHEST;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.cargoContainer);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        return this.tank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    public void readTankFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Empty")) {
            tag.removeTag("Empty");
        }
        tank.readFromNBT(tag);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tank.writeToNBT(tag);
    }

    public void writeEntityTankToNBT(NBTTagCompound tag) {
        tank.writeToNBT(tag);
    }

    public NBTTagCompound GetTag() {
        writeEntityTankToNBT(getEntityData());
        return getEntityData();
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }

}
