package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.block.transport.BlockFluidPipeGauge;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BEFluidPipeGauge extends BlockEntityFluidPipe {
    
    private Direction blockFacing;
    
    public BEFluidPipeGauge(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUIDPIPE_GAUGE_TILE.get(), pos, state);
    }
    
    public String getText() {
        return Utils.formatFluidAmountString(getMaster().averageFluid);
    }
    
    public float getOutPutAngle() {
        return Utils.normalizeClamped(getMaster().averageFluid, 0, MAX_TRANSFER) * 180f;
    }
    
    public Direction getBlockFacing() {
        if (blockFacing != null) return blockFacing;
        blockFacing = getBlockState().getValue(BlockFluidPipeGauge.FACING);
        return blockFacing;
    }
}
