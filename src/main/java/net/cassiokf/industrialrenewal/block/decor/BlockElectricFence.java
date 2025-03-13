package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockBasicElectricFence;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockElectricFence extends BlockBasicElectricFence {

    public BlockElectricFence()
    {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f), 2);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
    {
        if (isConnected(state, UP))
        {
            return 7;
        } else
        {
            return 0;
        }
    }

    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        if (neighborDirection == Direction.DOWN)
        {
            return false;
        }
        if (neighborDirection == Direction.UP)
        {
            int z = Math.abs(currentPos.getZ());
            int x = Math.abs(currentPos.getX());
            int w = x - z;
            return neighborState.isAir() && (Math.abs(w) % 3 == 0);
        }
        return nb instanceof BlockElectricFence
                || nb instanceof BlockElectricGate
                || neighborState.isFaceSturdy(worldIn, currentPos.relative(neighborDirection), neighborDirection)
                || nb instanceof BlockBasicElectricFence
                || nb instanceof BlockPillar
                || nb instanceof BlockColumn
//                || nb instanceof IRBaseWall
                ;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for (Direction face : Direction.Plane.HORIZONTAL) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        state = state.setValue(getPropertyBasedOnDirection(Direction.UP), canConnectTo(world, pos, Direction.UP));
        world.setBlock(pos, state, 3);
        super.neighborChanged(state, world, pos, block, neighborPos, flag);
    }

    @Override
    public boolean fenceCollision()
    {
        return true;
    }
}
