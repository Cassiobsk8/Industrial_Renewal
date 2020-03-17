package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable> implements ITickable
{

    public final VoltsEnergyContainer energyContainer;

    public int averageEnergy;
    private int oldEnergy;
    private int tick;

    private boolean inUse = false;

    public TileEntityEnergyCable()
    {
        this.energyContainer = new VoltsEnergyContainer(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport())
        {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate)
            {
                return TileEntityEnergyCable.this.onEnergyReceived(maxReceive, simulate);
            }
        };
    }

    @Override
    public void update()
    {
        if (!world.isRemote && isMaster())
        {
            if (tick >= 10)
            {
                tick = 0;
                averageEnergy = outPut / 10;
                outPut = 0;
                if (averageEnergy != oldEnergy)
                {
                    oldEnergy = averageEnergy;
                    Sync();
                }
            }
            tick++;
            limitedOutPutMap.clear();
        }
    }

    public abstract int getMaxEnergyToTransport();

    public int onEnergyReceived(int maxReceive, boolean simulate)
    {
        if (!isMaster()) return getMaster().onEnergyReceived(maxReceive, simulate);

        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;

        if (maxReceive <= 0) return 0;
        int out = 0;


        final Map<BlockPos, EnumFacing> mapPosSet = getMachinesPosSet();
        int quantity = mapPosSet.size();

        if (quantity > 0)
        {
            out = moveEnergy(maxReceive, simulate, mapPosSet);
            if (!simulate) outPut += out;
        }
        outPutCount = quantity;

        inUse = false;
        return out;
    }

    public int moveEnergy(int amount, boolean simulate, Map<BlockPos, EnumFacing> mapPosSet)
    {
        int out = 0;
        int validOutputs = getRealOutPutCount(mapPosSet);
        if (validOutputs == 0) return 0;
        int realMaxOutput = Math.min(amount / validOutputs, getMaxEnergyToTransport());

        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = mapPosSet.get(posM).getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    realMaxOutput = getLimitedValueForOutPut(realMaxOutput, energyContainer.getMaxOutput(), te.getPos(), simulate);
                    if (realMaxOutput > 0)
                    {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, simulate);
                        out += energy;
                    }
                }
            }
        }
        return out;
    }

    public int getRealOutPutCount(Map<BlockPos, EnumFacing> mapPosSet)
    {
        int canAccept = 0;
        int realMaxOutput = this.energyContainer.getMaxOutput();
        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = mapPosSet.get(posM).getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    realMaxOutput = getLimitedValueForOutPut(realMaxOutput, energyContainer.getMaxOutput(), te.getPos(), true);
                    if (realMaxOutput > 0)
                    {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, true);
                        if (energy > 0) canAccept++;
                    }
                }
            }
        }
        return canAccept;
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
                addMachine(currentPos, face);
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
        averageEnergy = compound.getInteger("energy_average");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy_average", averageEnergy);
        return super.writeToNBT(compound);
    }
}
