package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockFluidGauge;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockEntityFluidGauge extends BlockEntitySyncable {
    private Direction indicatorHorizontalFacing;
    private Direction baseFacing;
    private IFluidHandler fluidStorage;
    
    public BlockEntityFluidGauge(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUID_GAUGE.get(), pos, state);
    }
    
    public String getText() {
        if (getTankStorage() != null && getTankStorage().getTanks() > 0) {
            FluidStack stack = getTankStorage().getFluidInTank(0);
            return (stack != null && !stack.isEmpty()) ? stack.getDisplayName().getString() : "Empty";
        }
        return "No Tank";
    }
    
    public Direction getGaugeFacing() {
        if (indicatorHorizontalFacing != null) return indicatorHorizontalFacing;
        return forceIndicatorCheck();
    }
    
    public Direction forceIndicatorCheck() {
        indicatorHorizontalFacing = this.level.getBlockState(worldPosition).getValue(BlockFluidGauge.GAUGE);
        return indicatorHorizontalFacing;
    }
    
    public Direction getBaseFacing() {
        if (baseFacing != null) return baseFacing;
        BlockState state = level.getBlockState(worldPosition);
        if (state.getBlock() instanceof BlockFluidGauge) baseFacing = state.getValue(BlockFluidGauge.FACING);
        return baseFacing;
    }
    
    public void setBaseFacing(Direction baseFacing) {
        this.baseFacing = baseFacing;
    }
    
    public float getTankFill() //0 ~ 180
    {
        if (getTankStorage() != null && getTankStorage().getTanks() > 0) {
            float currentAmount = getTankStorage().getFluidInTank(0).getAmount() / 1000f;
            float totalCapacity = getTankStorage().getTankCapacity(0) / 1000f;
            currentAmount = currentAmount / totalCapacity;
            return currentAmount * 180f;
        }
        return 0;
    }
    
    private IFluidHandler getTankStorage() {
        if (fluidStorage != null) return fluidStorage;
        return forceCheck();
    }
    
    public IFluidHandler forceCheck() {
        if (level == null) return null;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(getBaseFacing()));
        if (te != null) {
            IFluidHandler handler = te.getCapability(ForgeCapabilities.FLUID_HANDLER, baseFacing.getOpposite()).orElse(null);
            if (handler != null) {
                fluidStorage = handler;
                return fluidStorage;
            }
        }
        fluidStorage = null;
        this.sync();
        return null;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putBoolean("fluid", fluidStorage != null);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        if (!compoundTag.getBoolean("fluid")) fluidStorage = null;
        super.load(compoundTag);
    }
}
