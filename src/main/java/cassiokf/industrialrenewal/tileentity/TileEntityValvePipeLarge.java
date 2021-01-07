package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
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

    public static final FluidTank dummyTank = new FluidTank(0)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }
    };

    public final FluidTank tank = new FluidTank(2000)
    {
        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            return TileEntityValvePipeLarge.this.passFluidOut(resource, doFill);
        }
    };

    @Override
    public void update()
    {
        if (this.hasWorld() && !world.isRemote && active)
        {
            EnumFacing faceToFill = getOutPutFace();
            TileEntity teIn = world.getTileEntity(pos.offset(faceToFill.getOpposite()));

            if (teIn != null)
            {
                IFluidHandler inTank = teIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill);
                if (inTank != null)
                {
                    inTank.drain(tank.fill(inTank.drain(Integer.MAX_VALUE, false), true), true);
                }
            }
        }
    }

    public int passFluidOut(FluidStack resource, boolean doFill)
    {
        if (!active) return 0;
        EnumFacing faceToFill = getOutPutFace();
        TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
        if (active && teOut != null)
        {
            IFluidHandler storage = teOut.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill.getOpposite());
            if (storage != null) return storage.fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public void playSwitchSound()
    {
        Random r = new Random();
        float pitch = r.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), IRSoundRegister.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F,
                pitch);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (facing == getOutPutFace())
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(dummyTank);
            if (facing == getOutPutFace().getOpposite())
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }
}