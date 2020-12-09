package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidGenerator
{
    private final TileEntitySync attachedTE;
    public final CustomFluidTank tank = new CustomFluidTank(IRConfig.Main.portableGeneratorCapacity.get())
    {
        @Override
        public void onContentsChanged()
        {
            FluidGenerator.this.sync();
        }
    };
    public static final int energyPerTick = IRConfig.Main.portableGeneratorEnergyPerTick.get();
    public final CustomEnergyStorage energyStorage = new CustomEnergyStorage(128, 128, energyPerTick)
    {
        @Override
        public void onEnergyChange()
        {
            FluidGenerator.this.markDirty();
        }

        @Override
        public boolean canExtract()
        {
            return false;
        }
    };
    private boolean canGenerate = false;
    private boolean isGenerating = false;
    private boolean oldRunning = false;
    private int fuelTime = 0;

    public FluidGenerator(TileEntitySync te)
    {
        attachedTE = te;
    }

    public void onTick()
    {
        if (canGenerate)
        {
            updateLiquidFuel();
            if (fuelTime > 0)
            {
                isGenerating = true;
                energyStorage.receiveInternally(energyPerTick, false);
                fuelTime--;
            } else isGenerating = false;
        } else isGenerating = false;

        if (isGenerating != oldRunning)
        {
            oldRunning = isGenerating;
            sync();
        }
    }

    public void markDirty()
    {
        attachedTE.markDirty();
    }

    public void sync()
    {
        attachedTE.sync();
    }

    private void updateLiquidFuel()
    {
        if (fuelTime > 0) return;
        FluidStack resource = tank.getFluid();
        if (resource == null || resource.getAmount() <= 0) return;

        int fuel = IRConfig.Main.fluidFuel.get(resource.getFluid().getName()) != null ? IRConfig.Main.fluidFuel.get(resource.getFluid().getName()) : 0;
        if (fuel > 0)
        {
            int amount = Math.min(References.BUCKET_VOLUME, resource.getAmount());
            float norm = Utils.normalize(amount, 0, References.BUCKET_VOLUME);

            fuelTime = (int) (fuel * norm) * 4;
            tank.drainInternal(amount, IFluidHandler.FluidAction.EXECUTE);
            //maxFuelTime = fuelTime;
            //fuelName = resource.getLocalizedName();
        }
    }

    public boolean isGenerating()
    {
        return isGenerating;
    }

    public CompoundNBT saveGenerator(CompoundNBT compound)
    {
        compound.putInt("FGenerator", fuelTime);
        compound.putBoolean("running", isGenerating);
        compound.putBoolean("generate", canGenerate);
        compound.put("energy", energyStorage.serializeNBT());
        compound.put("tank", tank.writeToNBT(new CompoundNBT()));
        return compound;
    }

    public void loadGenerator(CompoundNBT compound)
    {
        fuelTime = compound.getInt("FGenerator");
        isGenerating = compound.getBoolean("running");
        canGenerate = compound.getBoolean("generate");
        energyStorage.deserializeNBT(compound.getCompound("energy"));
        tank.readFromNBT(compound.getCompound("tank"));
    }

    public void setCanGenerate(boolean value)
    {
        canGenerate = value;
    }

    public void changeCanGenerate()
    {
        canGenerate = !canGenerate;
    }
}
