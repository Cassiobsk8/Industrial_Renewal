package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractSixWayConnections;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFluorescent extends BlockAbstractSixWayConnections {
    public BlockFluorescent()
    {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f).lightLevel((blockState)->15), 8, 6);
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 15;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        super.neighborChanged(state, world, pos, block, neighborPos, flag);
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
            world.setBlock(pos, state, 3);
        }
    }

    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        Block nb = neighborState.getBlock();
        if (neighborDirection != Direction.UP)
        {
            return nb instanceof BlockFluorescent;
        }
        return true;
        //return (currentPos.getZ() % 2) == 0;
    }
}
