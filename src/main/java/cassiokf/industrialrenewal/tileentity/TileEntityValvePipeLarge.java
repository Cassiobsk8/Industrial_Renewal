package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.IRSoundHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityValvePipeLarge extends TileEntityToggleableBase implements ITickable
{

    public static FluidTank dummyTank = new FluidTank(0)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }
    };

    public FluidTank tank = new FluidTank(2000)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityValvePipeLarge.this.markDirty();
        }
    };

    private int amountPerTick = 1000;

    @Override
    public void update()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            if (active)
            {
                EnumFacing faceToFill = getOutPutFace();
                TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
                TileEntity teIn = world.getTileEntity(pos.offset(faceToFill.getOpposite()));

                boolean hasFluidInternally = tank.getFluidAmount() > 0;

                if (teOut != null
                        && (hasFluidInternally
                        || (teIn != null && teIn.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill)))
                        && teOut.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                        faceToFill.getOpposite()))
                {
                    IFluidHandler inTank = hasFluidInternally
                            ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank)
                            : teIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill);
                    IFluidHandler outTank = teOut.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                            faceToFill.getOpposite());
                    if (inTank != null && outTank != null)
                    {
                        FluidStack amountCanFill = inTank.drain(amountPerTick, false);
                        if (amountCanFill != null) inTank.drain(outTank.fill(amountCanFill, true), true);
                    }
                }
            }
        }
    }

    @Override
    public void playSwitchSound()
    {
        Random r = new Random();
        float pitch = r.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), IRSoundHandler.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F,
                pitch);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        tank.readFromNBT(tag);
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tank.writeToNBT(tag);
        return super.writeToNBT(tag);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == getOutPutFace())
            return true;
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return isFacingEnabled(facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == getOutPutFace())
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(dummyTank);
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && isFacingEnabled(facing))
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        return super.getCapability(capability, facing);
    }
}