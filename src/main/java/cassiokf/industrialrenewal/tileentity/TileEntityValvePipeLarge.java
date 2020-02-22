package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Random;

import static cassiokf.industrialrenewal.init.TileRegistration.VALVELARGE_TILE;

public class TileEntityValvePipeLarge extends TileEntityToggleableBase implements ITickableTileEntity
{

    public static CustomFluidTank dummyTank = new CustomFluidTank(0)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }
    };

    public CustomFluidTank tank = new CustomFluidTank(2000)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityValvePipeLarge.this.markDirty();
        }
    };

    private int amountPerTick = 1000;

    public TileEntityValvePipeLarge()
    {
        super(VALVELARGE_TILE.get());
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            if (active)
            {
                Direction faceToFill = getOutPutFace();
                TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
                TileEntity teIn = world.getTileEntity(pos.offset(faceToFill.getOpposite()));

                boolean hasFluidInternally = tank.getFluidAmount() > 0;

                if (teOut != null
                        && (hasFluidInternally
                        || (teIn != null && teIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill).isPresent()))
                        && teOut.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill.getOpposite()).isPresent())
                {
                    IFluidHandler inTank = hasFluidInternally
                            ? tank
                            : teIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, faceToFill).orElse(null);
                    IFluidHandler outTank = teOut.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                            faceToFill.getOpposite()).orElse(null);
                    if (inTank != null && outTank != null)
                    {
                        FluidStack amountCanFill = inTank.drain(amountPerTick, IFluidHandler.FluidAction.SIMULATE);
                        if (amountCanFill != null)
                            inTank.drain(outTank.fill(amountCanFill, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
        this.getWorld().playSound(null, this.getPos(), SoundsRegistration.TILEENTITY_VALVE_CHANGE.get(), SoundCategory.BLOCKS, 1F,
                pitch);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        tank.readFromNBT(tag);
        super.read(tag);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        tank.writeToNBT(tag);
        return super.write(tag);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == getOutPutFace())
            return LazyOptional.of(() -> dummyTank).cast();
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && isFacingEnabled(facing))
            return LazyOptional.of(() -> tank).cast();
        return super.getCapability(capability, facing);
    }
}