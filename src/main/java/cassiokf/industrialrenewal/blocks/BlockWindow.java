package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockWindow extends BlockAbstractSixWayConnections
{

    public BlockWindow()
    {
        super(Block.Properties.create(Material.IRON), 2, 16);
    }


    private boolean shouldRenderCenter(IBlockReader world, BlockPos ownPos)
    {
        return (!isThisConnected(world, ownPos, Direction.SOUTH) && !isThisConnected(world, ownPos, Direction.NORTH) && !isThisConnected(world, ownPos, Direction.WEST) && !isThisConnected(world, ownPos, Direction.EAST))
                || ((isThisConnected(world, ownPos, Direction.SOUTH) && isThisConnected(world, ownPos, Direction.WEST))
                || (isThisConnected(world, ownPos, Direction.SOUTH) && isThisConnected(world, ownPos, Direction.EAST))
                || (isThisConnected(world, ownPos, Direction.NORTH) && isThisConnected(world, ownPos, Direction.WEST))
                || (isThisConnected(world, ownPos, Direction.NORTH) && isThisConnected(world, ownPos, Direction.EAST)))
                || (sidesConnected(world, ownPos) == 1);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 1.0F;
    }

    private int sidesConnected(IBlockReader world, BlockPos pos)
    {
        int sides = 0;
        for (Direction faces : Direction.Plane.HORIZONTAL)
        {
            BlockState neighborState = world.getBlockState(pos.offset(faces));
            Block nb = neighborState.getBlock();
            if (nb instanceof BlockWindow || neighborState.isSolid() || nb instanceof BlockBaseWall)
            {
                sides++;
            }
        }
        return sides;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(getPropertyBasedOnDirection(facing), canConnectTo(worldIn, currentPos, facing)).with(UP, shouldRenderCenter(worldIn, currentPos));
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        if (neighborDirection == Direction.DOWN)
        {
            return false;
        }

        if (neighborDirection == Direction.UP)
        {
            return shouldRenderCenter(worldIn, currentPos);
        }
        BlockState neighborState = worldIn.getBlockState(currentPos.offset(neighborDirection));
        Block nb = neighborState.getBlock();
        return nb instanceof BlockWindow
                || neighborState.isSolidSide(worldIn, currentPos.offset(neighborDirection), neighborDirection.getOpposite())
                || nb instanceof BlockBaseWall;
    }

    private boolean isThisConnected(IBlockReader world, BlockPos pos, Direction neighborFacing)
    {
        BlockState neighborState = world.getBlockState(pos.offset(neighborFacing));
        Block nb = neighborState.getBlock();
        return nb instanceof BlockWindow
                || neighborState.isSolidSide(world, pos.offset(neighborFacing), neighborFacing.getOpposite())
                || nb instanceof BlockBaseWall;
    }
}
