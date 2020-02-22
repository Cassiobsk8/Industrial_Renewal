package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.redstone.BlockSignalIndicator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class BlockBaseWall extends BlockAbstractFourConnections
{
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public BlockBaseWall()
    {
        super(Block.Properties.create(Material.ROCK), 8, 16, 24);
        setDefaultState(getDefaultState()
                .with(CORE, true)
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(EAST, false)
                .with(WEST, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CORE, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return false;
    }

    private boolean shouldRenderCenter(IBlockReader world, BlockPos ownPos)
    {
        return !((canCenterConnectTo(world, ownPos, Direction.NORTH) && canCenterConnectTo(world, ownPos, Direction.SOUTH) && !canCenterConnectTo(world, ownPos, Direction.EAST) && !canCenterConnectTo(world, ownPos, Direction.WEST))
                || (!canCenterConnectTo(world, ownPos, Direction.NORTH) && !canCenterConnectTo(world, ownPos, Direction.SOUTH) && canCenterConnectTo(world, ownPos, Direction.EAST) && canCenterConnectTo(world, ownPos, Direction.WEST)));
    }

    private boolean canCenterConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighborDirection)
    {
        final BlockPos neighborPos = ownPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        return nb instanceof BlockBaseWall || neighborState.isSolidSide(worldIn, neighborPos, neighborDirection.getOpposite()) || nb instanceof BlockWindow;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        return nb instanceof BlockBaseWall
                || neighborState.isSolidSide(worldIn, neighborPos, neighborDirection.getOpposite())
                || nb instanceof BlockElectricGate
                || nb instanceof BlockLight
                || nb instanceof BlockSignalIndicator
                || nb instanceof BlockWindow;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).with(CORE, shouldRenderCenter(context.getWorld(), context.getPos()));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos).with(CORE, shouldRenderCenter(worldIn, currentPos));
    }
}
