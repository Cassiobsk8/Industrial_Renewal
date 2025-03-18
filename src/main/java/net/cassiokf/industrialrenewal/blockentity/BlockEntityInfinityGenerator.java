package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEntityInfinityGenerator extends BlockEntity {
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(0, 0, 0) {
        @Override
        public boolean canReceive() {
            return false;
        }
        
        @Override
        public boolean canExtract() {
            return false;
        }
    };
    private final LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> energyContainer);
    
    public BlockEntityInfinityGenerator(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.INFINITY_GENERATOR.get(), pPos, pBlockState);
    }
    
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                updatePanel(facing, Integer.MAX_VALUE);
            }
        }
    }
    
    public void updatePanel(Direction facing, int energy) {
        final BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
        if (be != null && !be.isRemoved()) {
            be.getCapability(ForgeCapabilities.ENERGY, facing.getOpposite()).ifPresent(iEnergyStorage -> {
                iEnergyStorage.receiveEnergy(energy, false);
            });
        }
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY && side != Direction.UP && side != Direction.DOWN) {
            return this.optional.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optional.invalidate();
    }
}
