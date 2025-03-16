package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityFluidPipeBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.cassiokf.industrialrenewal.util.capability.CustomPressureFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;

public class BlockEntityHighPressureFluidPipe extends BlockEntityFluidPipeBase<BlockEntityHighPressureFluidPipe> {
    
    public static int MAX_PRESSURE_TRANSFER = 10000;
    private final CustomPressureFluidTank cTank = new CustomPressureFluidTank() {
        @Override
        public int receiveCompressedFluid(int amount, int y, FluidAction action) {
            return passCompressedFluid(amount, y, action);
        }
        
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return onFluidReceived(resource, action);
        }
    };
    
    public BlockEntityHighPressureFluidPipe(BlockPos pos, BlockState state) {
        super(ModBlockEntity.HIGH_PRESSURE_PIPE.get(), pos, state, MAX_PRESSURE_TRANSFER);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide() && isMaster()) {
            limitedOutPutMap.clear();
        }
    }
    
    @Override
    public CustomFluidTank getTank() {
        return cTank;
    }
    
    public int passCompressedFluid(int amount, int y, IFluidHandler.FluidAction action) {
        if (!isMaster() && !isMasterInvalid()) return getMaster().passCompressedFluid(amount, y, action);
        if (inUse || amount <= 0) return 0; //to prevent stack overflow (IE)
        inUse = true;
        int out = 0;
        final Map<BlockEntity, Direction> mapPosSet = getReceiversPressurizedContainer();
        int quantity = getRealQuantity(mapPosSet);
        
        if (quantity > 0) {
            out = moveFluid(amount, y, action, mapPosSet);
            if (action.equals(IFluidHandler.FluidAction.EXECUTE)) outPut += out;
        }
        outPutCount = quantity;
        
        inUse = false;
        return out;
    }
    
    public int moveFluid(int amount, int y, IFluidHandler.FluidAction action, Map<BlockEntity, Direction> mapPosSet) {
        int out = 0;
        int validOutputs = mapPosSet.size();
        if (validOutputs == 0) return 0;
        int realMaxOutput = Math.min(amount / validOutputs, MAX_PRESSURE_TRANSFER);
        for (BlockEntity te : mapPosSet.keySet()) {
            Direction face = mapPosSet.get(te).getOpposite();
            IFluidHandler f = te.getCapability(ForgeCapabilities.FLUID_HANDLER, face).orElse(null);
            if (f instanceof CustomPressureFluidTank c && c.canPassCompressedFluid()) {
                realMaxOutput = getLimitedValueForOutPut(realMaxOutput, MAX_PRESSURE_TRANSFER, te, action.equals(IFluidHandler.FluidAction.SIMULATE));
                if (realMaxOutput > 0) out += c.receiveCompressedFluid(realMaxOutput, y, action);
            }
        }
        return out;
    }
    
    private int getRealQuantity(Map<BlockEntity, Direction> mapPosSet) {
        cleanReceiversPressurizedContainer();
        return mapPosSet.size();
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
                boolean canReceivePressurized = fh instanceof CustomPressureFluidTank cf && cf.canPassCompressedFluid();
                if (hasMachine) {
                    if (canReceive) {
                        if (!isMasterInvalid()) getMaster().addReceiver(te, face);
                    } else if (!isMasterInvalid()) getMaster().removeReceiver(te);
                    if (canReceivePressurized) {
                        if (!isMasterInvalid()) getMaster().addReceiverPressurized(te, face);
                    } else if (!isMasterInvalid()) getMaster().removeReceiverPressurized(te);
                }
            }
            getMaster().cleanReceiversContainer();
            getMaster().cleanReceiversPressurizedContainer();
        }
    }
    
    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityHighPressureFluidPipe;// || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }
}
