package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockElectricFence extends BlockBasicElectricFence
{
    public BlockElectricFence()
    {
        super(Block.Properties.create(Material.IRON), 2);
    }

    @Override
    @Deprecated
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
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
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
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
            return nb.isAir(neighborState, worldIn, neighborPos) && (Math.abs(w) % 3 == 0);
        }
        return nb instanceof BlockElectricFence
                || nb instanceof BlockElectricGate
                || neighborState.isSolidSide(worldIn, currentPos.offset(neighborDirection), neighborDirection)
                || nb instanceof BlockBasicElectricFence;
    }

    @Override
    public boolean fenceCollision()
    {
        return true;
    }
}
