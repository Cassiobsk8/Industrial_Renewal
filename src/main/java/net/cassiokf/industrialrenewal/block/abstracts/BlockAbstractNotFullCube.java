package net.cassiokf.industrialrenewal.block.abstracts;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockAbstractNotFullCube extends IRBaseBlock {
    public BlockAbstractNotFullCube(Properties props) {
        super(props.noOcclusion());
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }
    
    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0f;
    }
}
