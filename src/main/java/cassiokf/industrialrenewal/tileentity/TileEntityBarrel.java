package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySyncable;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.init.TileRegistration.BARREL_TILE;

public class TileEntityBarrel extends TileEntitySyncable
{
    public CustomFluidTank tank = new CustomFluidTank(IRConfig.Main.barrelCapacity.get())
    {
        @Override
        public void onContentsChanged()
        {
            TileEntityBarrel.this.Sync();
        }
    };

    public TileEntityBarrel()
    {
        super(BARREL_TILE.get());
    }

    public String GetChatQuantity()
    {
        if (!tank.getFluid().isEmpty())
        {
            return tank.getFluid().getDisplayName().getString() + ": " + tank.getFluidAmount() + " mB";
        } else
        {
            return "Empty";
        }
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
