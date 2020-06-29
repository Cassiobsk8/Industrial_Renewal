package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidGenerator
{
    private final TileEntitySync attachedTE;
    public final FluidTank tank = new FluidTank(IRConfig.MainConfig.Main.portableGeneratorCapacity)
    {
        @Override
        public void onContentsChanged()
        {
            FluidGenerator.this.sync();
        }
    };
    public int energyPerTick = IRConfig.MainConfig.Main.portableGeneratorEnergyPerTick;
    public final VoltsEnergyContainer energyStorage = new VoltsEnergyContainer(128, 128, energyPerTick)
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
        if (resource == null || resource.amount <= 0) return;

        int fuel = IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getName()) != null ? IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getName()) : 0;
        if (fuel > 0)
        {
            int amount = Math.min(Fluid.BUCKET_VOLUME, resource.amount);
            float norm = Utils.normalize(amount, 0, Fluid.BUCKET_VOLUME);

            fuelTime = (int) (fuel * norm) * 4;
            tank.drainInternal(amount, true);
            //maxFuelTime = fuelTime;
            //fuelName = resource.getLocalizedName();
        }
    }

    public boolean isGenerating()
    {
        return isGenerating;
    }

    public NBTTagCompound saveGenerator(NBTTagCompound compound)
    {
        compound.setInteger("FGenerator", fuelTime);
        compound.setBoolean("running", isGenerating);
        compound.setBoolean("generate", canGenerate);
        compound.setTag("energy", energyStorage.serializeNBT());
        compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        return compound;
    }

    public void loadGenerator(NBTTagCompound compound)
    {
        fuelTime = compound.getInteger("FGenerator");
        isGenerating = compound.getBoolean("running");
        canGenerate = compound.getBoolean("generate");
        energyStorage.deserializeNBT(compound.getCompoundTag("energy"));
        tank.readFromNBT(compound.getCompoundTag("tank"));
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
