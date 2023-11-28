package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.IRBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFrame extends IRBaseBlock {
    public BlockFrame()
    {
        super(metalBasicProperties.noOcclusion());
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }
}
