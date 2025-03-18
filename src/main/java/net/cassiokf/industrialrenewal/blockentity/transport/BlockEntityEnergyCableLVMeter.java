package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.block.transport.BlockEnergyCableMeter;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnergyCableLVMeter extends BlockEntityEnergyCableLV {
    private Direction blockFacing;
    
    public BlockEntityEnergyCableLVMeter(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ENERGYCABLE_LV_METER_TILE.get(), pos, state);
    }
    
    @Override
    public Direction getBlockFacing() {
        if (blockFacing != null) return blockFacing;
        blockFacing = getBlockState().getValue(BlockEnergyCableMeter.FACING);
        return blockFacing;
    }
}
