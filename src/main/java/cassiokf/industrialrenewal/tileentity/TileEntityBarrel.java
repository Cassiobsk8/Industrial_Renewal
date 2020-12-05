package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySaveContent;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntitySaveContent
{
    public final CustomFluidTank tank = new CustomFluidTank(IRConfig.Main.barrelCapacity.get())
    {
        @Override
        public void onContentsChanged()
        {
            TileEntityBarrel.this.sync();
        }
    };

    public TileEntityBarrel(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public String GetChatQuantity()
    {
        if (this.tank.getFluid() != null)
        {
            return this.tank.getFluid().getDisplayName().getFormattedText() + ": " + this.tank.getFluidAmount() + " mB";
        } else
        {
            return "Empty";
        }
    }

    @Override
    public CustomFluidTank getTank()
    {
        return tank;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT tag = new CompoundNBT();
        tank.writeToNBT(tag);
        compound.put("fluid", tag);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT tag = compound.getCompound("fluid");
        tank.readFromNBT(tag);
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return LazyOptional.of(() -> tank).cast();
        }
        return super.getCapability(capability, facing);
    }
}
