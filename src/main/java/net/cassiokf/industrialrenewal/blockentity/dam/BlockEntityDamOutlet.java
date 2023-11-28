package net.cassiokf.industrialrenewal.blockentity.dam;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.capability.CustomCompressedFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDamOutlet extends BlockEntitySyncable {
    public static final int MAX_PROCESSING = 1000;
    private boolean hasFlow = false;
    private boolean removed = false;
    private Direction blockFacing;
    private int tick;
    
    private final CustomCompressedFluidTank tank = new CustomCompressedFluidTank(MAX_PROCESSING) {
        @Override
        public int receiveCompressedFluid(int amount, int y, FluidAction action) {
            return passCompressedFluid(amount, y, action);
        }
        
        @Override
        public boolean canDrain() {
            return false;
        }
        
        @Override
        public boolean canFill() {
            return false;
        }
    };
    
    public BlockEntityDamOutlet(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_OUTLET.get(), pos, state);
    }
    
    public void tick() {
        if (level == null) return;
        if (!level.isClientSide) {
            if (tick % 20 == 0) {
                tick = 0;
                BlockState state = level.getBlockState(worldPosition.relative(getBlockFacing()));
                if (hasFlow) {
                    if (removed && !state.getFluidState().isSource() && !state.getBlock().equals(Blocks.WATER)) {
                        BlockState waterState = Blocks.WATER.defaultBlockState();
                        level.setBlock(worldPosition.relative(getBlockFacing()), waterState, 3);
                        removed = false;
                    }
                } else {
                    if (!removed && (state.getFluidState().isSource() || state.getBlock().equals(Blocks.WATER))) {
                        level.setBlock(worldPosition.relative(getBlockFacing()), Blocks.AIR.defaultBlockState(), 3);
                        removed = true;
                    }
                }
                hasFlow = false;
            }
            tick ++;
        }
    }
    
    public int passCompressedFluid(int amount, int y, IFluidHandler.FluidAction action) {
        int height = y - worldPosition.getY();
        hasFlow = action.equals(IFluidHandler.FluidAction.EXECUTE) && amount >= 0 && height >= 0;
        return height >= 0 ? amount : 0;
    }
    
    public Direction getBlockFacing() {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }
    
    public Direction forceFaceCheck() {
        blockFacing = getBlockState().getValue(BlockAbstractHorizontalFacing.FACING);
        return blockFacing;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = getBlockFacing();
        if (side == null) return super.getCapability(cap, side);
        
        if (cap == ForgeCapabilities.FLUID_HANDLER && side.equals(facing.getOpposite()))
            return LazyOptional.of(() -> tank).cast();
        return super.getCapability(cap, side);
    }
}
