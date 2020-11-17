package cassiokf.industrialrenewal.tileentity.machines;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TEIndustrialBatteryBank extends TEMultiTankBase<TEIndustrialBatteryBank>
{
    public static final int capacity = IRConfig.MainConfig.Main.batteryBankCapacity * 100;
    private static final int maxTransfer = IRConfig.MainConfig.Main.industrialBatteryBankMaxTransfer;
    private final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(capacity, maxTransfer, maxTransfer)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }

        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public void onEnergyChange()
        {
            TEIndustrialBatteryBank.this.sync();
        }
    };
    private final VoltsEnergyContainer inEnergy = new VoltsEnergyContainer()
    {
        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return TEIndustrialBatteryBank.this.getBottomTE().energyContainer.receiveInternally(maxReceive, simulate);
        }
    };
    private int outPut;
    private int oldOutPut;

    @Override
    public void tick()
    {
        if (!world.isRemote && isMaster() && isBottom())
        {
            if (energyContainer.getEnergyStored() > 0)
            {
                TileEntity te = world.getTileEntity(getOutPutPos());
                if (te != null)
                {
                    IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
                    if (energyStorage != null)
                    {
                        outPut = energyContainer.extractEnergyInternally(energyStorage.receiveEnergy(energyContainer.extractEnergyInternally(maxTransfer, true), false), false);
                    } else outPut = 0;
                } else outPut = 0;
            } else outPut = 0;
            if (outPut != oldOutPut)
            {
                oldOutPut = outPut;
                sync();
            }
        }
    }

    private BlockPos getOutPutPos()
    {
        return getTopTE().pos.up(2).offset(getMasterFacing().rotateY());
    }

    @Override
    public void setSize(int i)
    {
        energyContainer.setMaxEnergyStored(capacity * i);
        sync();
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TEIndustrialBatteryBank;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(energyContainer.getEnergyStored()) + " / " + Utils.formatEnergyString(energyContainer.getMaxEnergyStored());
    }

    public float getBatteryFill()
    {
        return Utils.normalize(energyContainer.getEnergyStored(), 0, energyContainer.getMaxEnergyStored());
    }

    public float getOutPutAngle()
    {
        return Utils.normalize(outPut, 0, 10240) * 90;
    }

    public String getOutPutText()
    {
        return Utils.formatPreciseEnergyString(outPut) + "/t";
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.UP)
        {
            EnumFacing face = getMasterFacing();
            if (this.pos.equals(getMaster().pos.up().offset(face.rotateY()))) //out
                return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
            if (this.pos.equals(getMaster().pos.up().offset(face.rotateYCCW()))) //In
                return CapabilityEnergy.ENERGY.cast(getMaster().inEnergy);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        energyContainer.serializeNBT(compound);
        compound.setInteger("out", outPut);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        energyContainer.deserializeNBT(compound);
        outPut = compound.getInteger("out");
        super.readFromNBT(compound);
    }
}
