package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.obj.CapResult;
import net.cassiokf.industrialrenewal.util.PipeUtils;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlockEntityFluidPipeBase<T extends BlockEntityFluidPipeBase<?>> extends BlockEntityMultiBlocksTube<T> implements ICapabilityProvider {
    //TODO: add to config
    private int maxOutput = 1000;
    private int oldFluid;
    private int tick;
    public int averageFluid;
    
    public BlockEntityFluidPipeBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int maxOutput) {
        super(tileEntityTypeIn, pos, state);
        this.maxOutput = maxOutput;
    }
    
    private final CustomFluidTank tank = new CustomFluidTank(maxOutput) {
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return onFluidReceived(resource, action);
        }
    }.setBlockEntity(this);
    private final LazyOptional tankHandler = LazyOptional.of(this::getTank);
    
    @Override
    public void doTick() {
        if (!level.isClientSide && isMaster()) {
            if (tick >= 10) {
                tick = 0;
                averageFluid = outPut / 10;
                outPut = 0;
                if (averageFluid != oldFluid) {
                    oldFluid = averageFluid;
                    sync();
                }
            }
            tick++;
        }
    }
    
    public CustomFluidTank getTank() {
        return tank;
    }
    
    public int onFluidReceived(FluidStack resource, IFluidHandler.FluidAction action) {
        if (!isMaster() && !isMasterInvalid()) return getMaster().onFluidReceived(resource, action);
        
        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        if (resource == null || resource.getAmount() <= 0) {
            inUse = false;
            return 0;
        }
        CapResult result = PipeUtils.outputFluid(this, resource, maxOutput, action, level);
        if (action.equals(IFluidHandler.FluidAction.EXECUTE)) outPut += result.getOutPut();
        outPutCount = result.getValidReceivers();
        inUse = false;
        return result.getOutPut();
    }
    
    @Override
    public void checkForOutPuts(BlockPos bPos) {
        if (level == null) return;
        if (!level.isClientSide) {
            for (Direction face : Direction.values()) {
                BlockPos currentPos = worldPosition.relative(face);
                BlockEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = te != null && !(te instanceof BlockEntityFluidPipeBase);
                IFluidHandler fh = te != null ? te.getCapability(ForgeCapabilities.FLUID_HANDLER, face.getOpposite()).orElse(null) : null;
                boolean canReceive = fh != null && fh.getTankCapacity(0) > 0;
                if (canReceive && fh instanceof CustomFluidTank cf) canReceive = cf.canFill();
                if (hasMachine && canReceive) {
                    if (!isMasterInvalid()) getMaster().addReceiver(te, face);
                } else if (!isMasterInvalid()) getMaster().removeReceiver(te);
            }
            getMaster().cleanReceiversContainer();
        }
    }
    
    @Override
    public void invalidateCaps() {
        tankHandler.invalidate();
        super.invalidateCaps();
    }
    
    @NotNull
    @Override
    public <E> LazyOptional<E> getCapability(@NotNull Capability<E> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER && getMaster() != null)
            return LazyOptional.of(() -> getMaster().getTank()).cast();
        return super.getCapability(capability, facing);
    }
    
    @NotNull
    @Override
    public <E> LazyOptional<E> getCapability(@NotNull Capability<E> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && getMaster() != null)
            return LazyOptional.of(() -> getMaster().getTank()).cast();
        return super.getCapability(cap);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putInt("fluid", averageFluid);
        super.saveAdditional(compound);
    }
    
    @Override
    public void load(CompoundTag compound) {
        averageFluid = compound.getInt("fluid");
        super.load(compound);
    }
    
    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityFluidPipeBase;// || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }
}
