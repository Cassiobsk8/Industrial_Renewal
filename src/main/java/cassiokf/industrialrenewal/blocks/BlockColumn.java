package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.*;
import cassiokf.industrialrenewal.blocks.redstone.BlockAlarm;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.Objects;

public class BlockColumn extends BlockBase
{

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final IntegerProperty PIPE = IntegerProperty.create("pipe", 0, 2);

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.3125f;
    private static float UPY2 = 1.0f;


    public BlockColumn(Block.Properties property)
    {
        super(property);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST, PIPE);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    protected boolean isValidConnection(final BlockState neighbourState, final BlockState ownState, final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
    {
        Block nb = neighbourState.getBlock();

        if ((neighbourState.isSolid() || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable)
                && neighbourDirection != Direction.UP && neighbourDirection != Direction.DOWN)
        {

            Block oppositBlock = world.getBlockState(ownPos.offset(neighbourDirection.getOpposite())).getBlock();
            return oppositBlock instanceof BlockColumn || oppositBlock instanceof BlockPillar;
        }
        if (neighbourDirection != Direction.UP && neighbourDirection != Direction.DOWN)
        {
            if (nb instanceof BlockBrace)
            {
                return Objects.equals(neighbourState.get(BlockBrace.FACING).getName(), neighbourDirection.getOpposite().getName()) || Objects.equals(neighbourState.get(BlockBrace.FACING).getName(), "down_" + neighbourDirection.getName());
            }
            return nb instanceof BlockColumn || nb instanceof BlockPillar
                    || (nb instanceof BlockWireBase && neighbourState.get(BlockWireBase.FACING) == neighbourDirection.getOpposite())
                    || nb instanceof BlockPillarEnergyCable || nb instanceof BlockPillarFluidPipe
                    || (nb instanceof BlockAlarm && neighbourState.get(BlockAlarm.FACING) == neighbourDirection)
                    || (nb instanceof BlockLight && neighbourState.get(BlockLight.FACING) == neighbourDirection.getOpposite());
        }
        if (nb instanceof BlockLight)
        {
            return neighbourState.get(BlockLight.FACING) == Direction.UP;
        }
        if (nb instanceof BlockBrace)
        {
            return Direction.Plane.HORIZONTAL.test(neighbourState.get(BlockBrace.FACING).getFacing());
        }
        if (nb instanceof BlockFluidPipe)
        {
            return ownState.get(PIPE) > 0;
        }
        if (nb instanceof BlockCableTray)
        {
            return neighbourState.get(BlockCableTray.BASE).equals(EnumBaseDirection.UP);
        }
        return !(nb instanceof BlockCatwalkLadder)
                && !(nb instanceof BlockSignBase)
                && !(nb instanceof BlockFireExtinguisher)
                && !(nb instanceof BlockAlarm && !(neighbourState.get(BlockAlarm.FACING) == neighbourDirection))
                && !nb.isAir(neighbourState, world, ownPos.offset(neighbourDirection));
    }

    private boolean canConnectTo(final BlockState ownState, final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        final Block neighbourBlock = neighbourState.getBlock();

        final boolean neighbourIsValidForThis = isValidConnection(neighbourState, ownState, worldIn, ownPos, neighbourDirection);
        final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockColumn) || ((BlockColumn) neighbourBlock).isValidConnection(ownState, neighbourState, worldIn, neighbourPos, neighbourDirection.getOpposite());

        return neighbourIsValidForThis && thisIsValidForNeighbour;
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
        stateIn = stateIn.with(UP, canConnectTo(stateIn, worldIn, currentPos, Direction.UP)).with(DOWN, canConnectTo(stateIn, worldIn, currentPos, Direction.DOWN))
                .with(NORTH, canConnectTo(stateIn, worldIn, currentPos, Direction.NORTH)).with(SOUTH, canConnectTo(stateIn, worldIn, currentPos, Direction.SOUTH))
                .with(EAST, canConnectTo(stateIn, worldIn, currentPos, Direction.EAST)).with(WEST, canConnectTo(stateIn, worldIn, currentPos, Direction.WEST));

        return stateIn;
    }

    private boolean pipeConnected(IBlockReader world, BlockPos pos, BlockState state, Direction facing)
    {
        Block blockOffset = world.getBlockState(pos.offset(facing)).getBlock();
        return blockOffset.equals(state.getBlock());
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        if (facing == Direction.UP)
        {
            return state.get(UP);
        }
        if (facing == Direction.DOWN)
        {
            return state.get(DOWN);
        }
        if (facing == Direction.NORTH)
        {
            return state.get(NORTH);
        }
        if (facing == Direction.SOUTH)
        {
            return state.get(SOUTH);
        }
        if (facing == Direction.EAST)
        {
            return state.get(EAST);
        }
        return state.get(WEST);
        //return state.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        if (isConnected(state, Direction.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, Direction.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(state, Direction.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, Direction.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(state, Direction.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, Direction.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(state, Direction.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, Direction.EAST)) {
            EASTX2 = 0.750f;
        }
        if (isConnected(state, Direction.DOWN)) {
            DOWNY1 = 0.0f;
        } else if (!isConnected(state, Direction.DOWN)) {
            DOWNY1 = 0.3125f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        BlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, Direction.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, Direction.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(actualState, Direction.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, Direction.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(actualState, Direction.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, Direction.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(actualState, Direction.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, Direction.EAST)) {
            EASTX2 = 0.750f;
        }
        if (isConnected(actualState, Direction.DOWN)) {
            DOWNY1 = 0.0f;
        } else if (!isConnected(actualState, Direction.DOWN)) {
            DOWNY1 = 0.3125f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }
*/
}
