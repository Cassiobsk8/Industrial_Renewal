package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySaveContent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntitySaveContent
{
    public FluidTank tank = new FluidTank(IRConfig.MainConfig.Main.barrelCapacity)
    {
        @Override
        public void onContentsChanged()
        {
            TileEntityBarrel.this.sync();
        }
    };

    public String GetChatQuantity()
    {
        if (this.tank.getFluid() != null)
        {
            return this.tank.getFluid().getLocalizedName() + ": " + this.tank.getFluidAmount() + " mB";
        } else
        {
            return "Empty";
        }
    }

    @Override
    public FluidTank getTank()
    {
        return tank;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        compound.setTag("fluid", tag);

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = compound.getCompoundTag("fluid");
        tank.readFromNBT(tag);

        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }
}
