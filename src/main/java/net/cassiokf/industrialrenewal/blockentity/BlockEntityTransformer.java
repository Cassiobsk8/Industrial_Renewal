package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockTransformer;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x2x3MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockEntityTransformer extends BlockEntity3x2x3MachineBase<BlockEntityTransformer> {
    
    private final int MAX_CAPACITY = 10000;
    private final int TRANSFER_SPEED = 10000;
    private final CustomEnergyStorage energyDummy = new CustomEnergyStorage(0);
    private final LazyOptional<CustomEnergyStorage> dummyHandler = LazyOptional.of(() -> energyDummy);
    private int tick = 0;
    private int averageEnergy = 0;
    private int energyOverTime = 0;
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(MAX_CAPACITY, TRANSFER_SPEED, TRANSFER_SPEED) {
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return distributeEnergy(maxReceive, simulate);
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }
    };
    private final LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    
    public BlockEntityTransformer(BlockPos pos, BlockState state) {
        super(ModBlockEntity.TRANSFORMER_TILE.get(), pos, state);
    }
    
    public void tick() {
        if (level != null && !level.isClientSide) {
            if (tick % 10 == 0) {
                tick = 0;
                int old = averageEnergy;
                averageEnergy = energyOverTime / 10;
                energyOverTime = 0;
                if (averageEnergy != old) {
                    sync();
                }
            }
            tick++;
        }
    }
    
    private boolean extract() {
        return getBlockState().is(ModBlocks.TRANSFORMER.get()) && getBlockState().getValue(BlockTransformer.OUTPUT) == 2;
    }
    
    private Direction getFacing() {
        return getBlockState().is(ModBlocks.TRANSFORMER.get()) ? getBlockState().getValue(BlockTransformer.FACING) : Direction.NORTH;
    }
    
    private int distributeEnergy(int energy, boolean simulate) {
        BlockEntityHVIsolator isolator = getIsolator();
        int out = 0;
        if (isolator != null) {
            Set<BlockPos> allNodesPos = isolator.allNodes;
            Set<BlockEntityTransformer> availableTransformers = allNodesPos.stream().filter(x -> level.getBlockEntity(x.below()) instanceof BlockEntityTransformer).map(x -> (BlockEntityTransformer) level.getBlockEntity(x.below())).filter(Objects::nonNull).filter(x -> x.isMaster() && x.extract()).collect(Collectors.toSet());
            if (availableTransformers.isEmpty()) return 0;
            int quota = Math.min(TRANSFER_SPEED, energy) / availableTransformers.size();
            
            for (BlockEntityTransformer transformer : availableTransformers) {
                if (transformer != null) {
                    int amount = transformer.outPutEnergy(quota, simulate);
                    //Utils.debug("REQUEST", worldPosition, transformer, quota, amount, this.energyStorage.getMaxEnergyStored()-this.energyStorage.getEnergyStored());
                    //quota += (quota - amount);
                    out += amount;
                }
                if (quota <= 0) break;
            }
        }
        if (!simulate) energyOverTime += out;
        return out;
    }
    
    private int outPutEnergy(int energy, boolean simulate) {
        BlockPos targetLocation = worldPosition.below().relative(getFacing().getOpposite(), 2);
        BlockEntity te = level.getBlockEntity(targetLocation);
        if (te != null) {
            IEnergyStorage teEnergyStorage = te.getCapability(ForgeCapabilities.ENERGY, getFacing()).orElse(null);
            if (teEnergyStorage != null) {
                int amount = teEnergyStorage.receiveEnergy(Math.min(TRANSFER_SPEED, energy), simulate);
                if (!simulate) energyOverTime += amount;
                return amount;
            }
        }
        return 0;
    }
    
    private boolean hasIsolator() {
        if (level == null) return false;
        return level.getBlockEntity(worldPosition.above()) instanceof BlockEntityHVIsolator;
    }
    
    private BlockEntityHVIsolator getIsolator() {
        if (level == null) return null;
        BlockEntity TE = level.getBlockEntity(worldPosition.above());
        if (TE instanceof BlockEntityHVIsolator) return (BlockEntityHVIsolator) TE;
        return null;
    }
    
    public String getGenerationText() {
        return (extract() ? "IN" : "OUT") + " " + averageEnergy + " FE/t";
    }
    
    public float getGenerationFill() {
        float currentAmount = averageEnergy;
        float totalCapacity = MAX_CAPACITY;
        //        Utils.debug("G", currentAmount, totalCapacity);
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction masterFacing = getFacing();
        BlockPos masterPos = super.masterPos != null ? super.masterPos : worldPosition;
        
        if (side == null) return super.getCapability(cap, side);
        
        if (cap == ForgeCapabilities.ENERGY) {
            if (worldPosition.equals(masterPos.below().relative(masterFacing.getOpposite())) && side == masterFacing.getOpposite()) {
                if (getMaster() != null) {
                    if (getMaster().extract()) return getMaster().dummyHandler.cast();
                    else return getMaster().energyStorageHandler.cast();
                }
            }
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", averageEnergy);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        averageEnergy = compoundTag.getInt("energy");
        super.load(compoundTag);
    }
}
