package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.*;
import cassiokf.industrialrenewal.blocks.redstone.BlockAlarm;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.Objects;

public class BlockColumn extends BlockAbstractSixWayConnections
{
    public static final IntegerProperty PIPE = IntegerProperty.create("pipe", 0, 2);

    public BlockColumn()
    {
        super(Block.Properties.create(Material.IRON), 8, 12);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST, PIPE);
    }

    protected boolean isValidConnection(final BlockState neighborState, final BlockState ownState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();

        if ((neighborState.isSolid() || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable)
                && neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {

            Block oppositBlock = world.getBlockState(ownPos.offset(neighborDirection.getOpposite())).getBlock();
            return oppositBlock instanceof BlockColumn || oppositBlock instanceof BlockPillar;
        }
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            if (nb instanceof BlockBrace)
            {
                return Objects.equals(neighborState.get(BlockBrace.FACING).getName(), neighborDirection.getOpposite().getName()) || Objects.equals(neighborState.get(BlockBrace.FACING).getName(), "down_" + neighborDirection.getName());
            }
            return nb instanceof BlockColumn || nb instanceof BlockPillar
                    || (nb instanceof BlockHVIsolator && neighborState.get(BlockHVIsolator.FACING) == neighborDirection.getOpposite())
                    || nb instanceof BlockPillarEnergyCable || nb instanceof BlockPillarFluidPipe
                    || (nb instanceof BlockAlarm && neighborState.get(BlockAlarm.FACING) == neighborDirection)
                    || (nb instanceof BlockLight && neighborState.get(BlockLight.FACING) == neighborDirection.getOpposite());
        }
        if (nb instanceof BlockLight)
        {
            return neighborState.get(BlockLight.FACING) == Direction.UP;
        }
        if (nb instanceof BlockBrace)
        {
            return Direction.Plane.HORIZONTAL.test(neighborState.get(BlockBrace.FACING).getFacing());
        }
        if (nb instanceof BlockFluidPipe)
        {
            return ownState.get(PIPE) > 0;
        }
        if (nb instanceof BlockCableTray)
        {
            return neighborState.get(BlockCableTray.BASE).equals(EnumBaseDirection.UP);
        }
        return !(nb instanceof BlockCatwalkLadder)
                && !(nb instanceof BlockSignBase)
                && !(nb instanceof BlockFireExtinguisher)
                && !(nb instanceof BlockAlarm && !(neighborState.get(BlockAlarm.FACING) == neighborDirection))
                && !nb.isAir(neighborState, world, ownPos.offset(neighborDirection));
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        BlockState ownState = worldIn.getBlockState(currentPos);
        return isValidConnection(neighborState, ownState, worldIn, currentPos, neighborDirection);
    }

    private int canConnectPipe(IBlockReader world, BlockPos pos)
    {
        BlockState stateOffset = world.getBlockState(pos.down());
        if (stateOffset.getBlock() instanceof BlockPipeBase && !(stateOffset.getBlock() instanceof BlockCableTray))
        {
            if (pipeConnected(world, pos.down(), stateOffset, Direction.DOWN))
            {
                return 0;
            }
            if (!pipeConnected(world, pos.down(), stateOffset, Direction.NORTH) && !pipeConnected(world, pos.down(), stateOffset, Direction.SOUTH))
            {
                return 2;
            }
            if (!pipeConnected(world, pos.down(), stateOffset, Direction.EAST) && !pipeConnected(world, pos.down(), stateOffset, Direction.WEST))
            {
                return 1;
            }
        }
        return 0;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        stateIn = stateIn.with(PIPE, canConnectPipe(worldIn, currentPos));
        stateIn = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        return stateIn;
    }

    private boolean pipeConnected(IBlockReader world, BlockPos pos, BlockState state, Direction facing)
    {
        Block blockOffset = world.getBlockState(pos.offset(facing)).getBlock();
        return blockOffset.equals(state.getBlock());
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        switch (facing)
        {
            case DOWN:
                return state.get(DOWN);
            case UP:
                return state.get(UP);
            default:
            case NORTH:
                return state.get(NORTH);
            case SOUTH:
                return state.get(SOUTH);
            case WEST:
                return state.get(WEST);
            case EAST:
                return state.get(EAST);
        }
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        float NORTHZ1;
        float SOUTHZ2;
        float WESTX1;
        float EASTX2;
        float DOWNY1;

        if (isConnected(state, Direction.NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 4;
        }
        if (isConnected(state, Direction.SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = 12;
        }
        if (isConnected(state, Direction.WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 4;
        }
        if (isConnected(state, Direction.EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = 12;
        }
        if (isConnected(state, Direction.DOWN))
        {
            DOWNY1 = 0;
        } else
        {
            DOWNY1 = 5;
        }
        return Block.makeCuboidShape(WESTX1, DOWNY1, NORTHZ1, EASTX2, 16, SOUTHZ2);
    }
}
