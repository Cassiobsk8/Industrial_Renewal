package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityValvePipeLarge extends TileEntityToggleableBase implements ITickableTileEntity
{

    public static final CustomFluidTank dummyTank = new CustomFluidTank(0)
    {
        @Override
        public boolean canFill(FluidStack stack)
        {
            return false;
        }
    };

    public final CustomFluidTank tank = new CustomFluidTank(2000)
    {
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return TileEntityValvePipeLarge.this.passFluidOut(resource, action);
        }
    };

    public TileEntityValvePipeLarge(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !world.isRemote && active)
        {
            Direction faceToFill = getOutPutFace();
            TileEntity teIn = world.getTileEntity(pos.offset(faceToFill.getOpposite()));

            if (teIn != null)
            {
                IFluidHandler inTank = teIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill).orElse(null);
                if (inTank != null)
                {
                    inTank.drain(tank.fill(inTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public int passFluidOut(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (!active || resource == null || resource.amount <= 0) return 0;
        Direction faceToFill = getOutPutFace();
        TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
        if (teOut != null)
        {
            IFluidHandler storage = teOut.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill.getOpposite()).orElse(null);
            if (storage != null) return storage.fill(resource, action);
        }
        return 0;
    }

    @Override
    public void playSwitchSound()
    {
        float pitch = rand.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), SoundsRegistration.TILEENTITY_VALVE_CHANGE.get(), SoundCategory.BLOCKS, 1F,
                pitch);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (facing == getOutPutFace())
                return LazyOptional.of(() -> dummyTank).cast();
            if (facing == getOutPutFace().getOpposite())
                return LazyOptional.of(() -> tank).cast();
        }
        return super.getCapability(capability, facing);
    }
}