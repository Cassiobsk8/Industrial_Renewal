package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable> implements ICapabilityProvider
{

    public final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);


    public TileEntityEnergyCable(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(10240, getMaxEnergyToTransport(), getMaxEnergyToTransport())
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
    public void doTick()
    {
        if (!world.isRemote && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            IEnergyStorage thisStorage = energyStorage.orElse(null);
            energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(getMaxEnergyToTransport() * quantity, thisStorage.getEnergyStored())));

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

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(MASTER, IRConfig.Main.showMaster.get() && isMaster())
                .withInitial(SOUTH, canConnectToPipe(Direction.SOUTH))
                .withInitial(NORTH, canConnectToPipe(Direction.NORTH))
                .withInitial(EAST, canConnectToPipe(Direction.EAST))
                .withInitial(WEST, canConnectToPipe(Direction.WEST))
                .withInitial(UP, canConnectToPipe(Direction.UP))
                .withInitial(DOWN, canConnectToPipe(Direction.DOWN))
                .withInitial(CSOUTH, canConnectToCapability(Direction.SOUTH))
                .withInitial(CNORTH, canConnectToCapability(Direction.NORTH))
                .withInitial(CEAST, canConnectToCapability(Direction.EAST))
                .withInitial(CWEST, canConnectToCapability(Direction.WEST))
                .withInitial(CUP, canConnectToCapability(Direction.UP))
                .withInitial(CDOWN, canConnectToCapability(Direction.DOWN))
                .build();
    }

    public boolean canConnectToPipe(Direction neighborDirection)
    {
        BlockPos otherPos = pos.offset(neighborDirection);
        TileEntity te = world.getTileEntity(otherPos);
        return instanceOf(te);
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos otherPos = pos.offset(neighborDirection);
        BlockState state = world.getBlockState(otherPos);
        TileEntity te = world.getTileEntity(otherPos);
        return !(state.getBlock() instanceof BlockEnergyCable)
                && te != null
                && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }

    public int moveEnergy(boolean simulate, int validOutputs, Map<BlockPos, Direction> mapPosSet)
    {
        int canAccept = 0;
        int out = 0;
        IEnergyStorage thisStorage = energyStorage.orElse(null);
        int realMaxOutput = Math.min(thisStorage.getEnergyStored() / validOutputs, getMaxEnergyToTransport());
        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            Direction face = mapPosSet.get(posM).getOpposite();
            if (te != null)
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face).orElse(null);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    int energy = energyStorage.receiveEnergy(thisStorage.extractEnergy(realMaxOutput, true), simulate);
                    if (simulate)
                    {
                        if (energy > 0) canAccept++;
                    } else
                    {
                        out += energy;
                        thisStorage.extractEnergy(energy, false);
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
        for (Direction face : Direction.values())
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null
                    && !(te instanceof TileEntityEnergyCable)
                    && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();
            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
        }
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && getMaster() != null)
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        return super.write(compound);
    }
}
