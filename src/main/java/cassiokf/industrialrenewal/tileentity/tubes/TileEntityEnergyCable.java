package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public abstract class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable> implements ICapabilityProvider
{

    public final VoltsEnergyContainer energyContainer;


    public TileEntityEnergyCable()
    {
        this.energyContainer = new VoltsEnergyContainer(10240, getMaxEnergyToTransport(), getMaxEnergyToTransport())
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityEnergyCable.this.markDirty();
            }
        };
    }

    public abstract int getMaxEnergyToTransport();

    @Override
    public void update()
    {
        if (!world.isRemote && isMaster())
        {
            int quantity = getPosSet().size();
            this.energyContainer.setMaxEnergyStored(Math.max(this.energyContainer.getMaxOutput() * quantity, this.energyContainer.getEnergyStored()));

            int canAccept = moveEnergy(true, 1);
            outPut = canAccept > 0 ? moveEnergy(false, canAccept) : 0;

            outPutCount = getPosSet().size();
            if ((oldOutPut != outPut) || (oldOutPutCount != outPutCount))
            {
                oldOutPut = outPut;
                oldOutPutCount = outPutCount;
                this.Sync();
            }
        }
    }

    public int moveEnergy(boolean simulate, int validOutputs)
    {
        int canAccept = 0;
        int out = 0;
        int realMaxOutput = Math.min(energyContainer.getEnergyStored() / validOutputs, this.energyContainer.getMaxOutput());
        for (BlockPos posM : getPosSet().keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = getPosSet().get(posM).getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    int energy = energyStorage.receiveEnergy(this.energyContainer.extractEnergy(realMaxOutput, true), simulate);
                    if (simulate)
                    {
                        if (energy > 0) canAccept++;
                    } else
                    {
                        out += energy;
                        this.energyContainer.extractEnergy(energy, false);
                    }
                }
            }
        }
        return simulate ? canAccept : out;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null
                    && !(te instanceof TileEntityEnergyCable)
                    && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite());
            IEnergyStorage eStorage = null;
            if (hasMachine) eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
            if (hasMachine && eStorage != null && eStorage.canReceive())
                getMaster().addMachine(currentPos, face);
            else getMaster().removeMachine(pos, currentPos);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && getMaster() != null)
            return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }
}
