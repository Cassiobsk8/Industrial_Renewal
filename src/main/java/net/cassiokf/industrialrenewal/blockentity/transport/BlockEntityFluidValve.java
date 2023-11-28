package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeSwitchBase;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityFluidValve extends BlockEntitySyncable {
    public BlockEntityFluidValve(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUID_VALVE_TILE.get(), pos, state);
    }
    
    private final CustomFluidTank dummyTank = new CustomFluidTank(0);
    private final CustomFluidTank fluidTank = new CustomFluidTank(0) {
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !BlockEntityFluidValve.this.isOpen()) return 0;
            IFluidHandler out = BlockEntityFluidValve.this.getOutput();
            if (out != null) {
                return out.fill(resource, action);
            }
            return 0;
        }
        
        @Override
        public boolean canFill() {
            return isOpen();
        }
        
        @Override
        public boolean canDrain() {
            return false;
        }
    };

    private final LazyOptional<CustomFluidTank> tankLazyOptional = LazyOptional.of(()-> fluidTank);
    private final LazyOptional<CustomFluidTank> dummyLazyOptional = LazyOptional.of(()-> dummyTank);
    
    private IFluidHandler getOutput() {
        if (level == null) return null;
        Direction facing = getFacing();
        BlockEntity outputTile = level.getBlockEntity(worldPosition.relative(facing));
        if (outputTile != null) {
            return outputTile.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).orElse(null);
        }
        return null;
    }
    
    private boolean isOpen(){
        return getBlockState().is(ModBlocks.FLUID_VALVE.get())? getBlockState().getValue(BlockPipeSwitchBase.ON_OFF) : false;
    }
    
    private Direction getFacing(){
        return getBlockState().is(ModBlocks.FLUID_VALVE.get())? getBlockState().getValue(BlockPipeSwitchBase.FACING) : Direction.NORTH;
    }
    
    @Override
    public void invalidateCaps() {
        tankLazyOptional.invalidate();
        dummyLazyOptional.invalidate();
        super.invalidateCaps();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = getFacing();

        if (side == null)
            return super.getCapability(cap, side);

        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == facing.getOpposite()) return tankLazyOptional.cast();
            else if (side == facing) return dummyLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
