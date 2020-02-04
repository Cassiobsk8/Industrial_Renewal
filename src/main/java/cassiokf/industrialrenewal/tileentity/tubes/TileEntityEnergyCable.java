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
import java.util.Map;

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
            final Map<BlockPos, EnumFacing> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            this.energyContainer.setMaxEnergyStored(Math.max(this.energyContainer.getMaxOutput() * quantity, this.energyContainer.getEnergyStored()));

            if (quantity > 0)
            {
                int canAccept = moveEnergy(true, 1, mapPosSet);
                outPut = canAccept > 0 ? moveEnergy(false, canAccept, mapPosSet) : 0;
            } else outPut = 0;

            outPutCount = quantity;
            if ((oldOutPut != outPut) || (oldOutPutCount != outPutCount))
            {
                oldOutPut = outPut;
                oldOutPutCount = outPutCount;
                this.Sync();
            }
        }
    }

    public int moveEnergy(boolean simulate, int validOutputs, Map<BlockPos, EnumFacing> mapPosSet)
    {
        int canAccept = 0;
        int out = 0;
        int realMaxOutput = Math.min(energyContainer.getEnergyStored() / validOutputs, this.energyContainer.getMaxOutput());
        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = mapPosSet.get(posM).getOpposite();
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
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
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
