package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeSwitchBase;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityEnergySwitch extends BlockEntitySyncable {
    private final CustomEnergyStorage dummyStorage = new CustomEnergyStorage(0);
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (maxReceive <= 0 || !BlockEntityEnergySwitch.this.isOpen()) return 0;
            IEnergyStorage out = BlockEntityEnergySwitch.this.getOutput();
            if (out != null) {
                return out.receiveEnergy(maxReceive, simulate);
            }
            return 0;
        }
        
        @Override
        public boolean canReceive() {
            return isOpen();
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }
        
        @Override
        public boolean canExtract() {
            return false;
        }
    };
    private final LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    private final LazyOptional<CustomEnergyStorage> dummyStorageHandler = LazyOptional.of(() -> dummyStorage);
    public BlockEntityEnergySwitch(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ENERGY_SWITCH_TILE.get(), pos, state);
    }
    
    private IEnergyStorage getOutput() {
        if (level == null) return null;
        Direction facing = getFacing();
        BlockEntity outputTile = level.getBlockEntity(worldPosition.relative(facing));
        if (outputTile != null) {
            return outputTile.getCapability(ForgeCapabilities.ENERGY, facing.getOpposite()).orElse(null);
        }
        return null;
    }
    
    private boolean isOpen() {
        return getBlockState().is(ModBlocks.ENERGY_SWITCH.get()) ? getBlockState().getValue(BlockPipeSwitchBase.ON_OFF) : false;
    }
    
    private Direction getFacing() {
        return getBlockState().is(ModBlocks.ENERGY_SWITCH.get()) ? getBlockState().getValue(BlockPipeSwitchBase.FACING) : Direction.NORTH;
    }
    
    @Override
    public void invalidateCaps() {
        energyStorageHandler.invalidate();
        dummyStorageHandler.invalidate();
        super.invalidateCaps();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = getFacing();
        
        if (side == null) return super.getCapability(cap, side);
        
        if (cap == ForgeCapabilities.ENERGY) {
            if (side == facing.getOpposite()) return energyStorageHandler.cast();
            else if (side == facing) return dummyStorageHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
