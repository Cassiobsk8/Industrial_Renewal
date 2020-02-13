package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockElectricBigFenceColumn extends BlockBasicElectricFence
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 2);

    //public static final IUnlistedProperty<Boolean> CORE = new Properties.PropertyAdapter<>(BooleanProperty.create("core"));
    //public static final IUnlistedProperty<Boolean> ACTIVE_LEFT = new Properties.PropertyAdapter<>(BooleanProperty.create("active_left"));
    //public static final IUnlistedProperty<Boolean> ACTIVE_RIGHT = new Properties.PropertyAdapter<>(BooleanProperty.create("active_right"));
    //public static final IUnlistedProperty<Boolean> ACTIVE_LEFT_TOP = new Properties.PropertyAdapter<>(BooleanProperty.create("active_left_top"));
    //public static final IUnlistedProperty<Boolean> ACTIVE_RIGHT_TOP = new Properties.PropertyAdapter<>(BooleanProperty.create("active_right_top"));
    //public static final IUnlistedProperty<Boolean> ACTIVE_LEFT_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("active_left_down"));
    //public static final IUnlistedProperty<Boolean> ACTIVE_RIGHT_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("active_right_down"));


    public BlockElectricBigFenceColumn(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(INDEX) == 0)
        {
            worldIn.setBlockState(pos.up(), state.with(INDEX, 1));
            worldIn.setBlockState(pos.up(2), state.with(INDEX, 2));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        switch (state.get(INDEX))
        {
            case 0:
                if (IsBigFence(worldIn, pos.up())) worldIn.removeBlock(pos.up(), false);
                if (IsBigFence(worldIn, pos.up(2))) worldIn.removeBlock(pos.up(2), false);
                break;
            case 1:
                if (IsBigFence(worldIn, pos.down())) worldIn.removeBlock(pos.down(), false);
                if (IsBigFence(worldIn, pos.up())) worldIn.removeBlock(pos.up(), false);
                break;
            case 2:
                if (IsBigFence(worldIn, pos.down())) worldIn.removeBlock(pos.down(), false);
                if (IsBigFence(worldIn, pos.down(2))) worldIn.removeBlock(pos.down(2), false);
                break;
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state;
    }

    public boolean IsBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceColumn;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up()).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up(2)).getMaterial().isReplaceable();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{FACING, INDEX}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{CORE, ACTIVE_LEFT, ACTIVE_RIGHT, ACTIVE_LEFT_TOP, ACTIVE_RIGHT_TOP, ACTIVE_LEFT_DOWN, ACTIVE_RIGHT_DOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    }

    @Override
    @Deprecated
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return state.get(INDEX) == 2 ? 15 : 0;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(INDEX, 0);
    }

    public boolean ActiveSide(IBlockReader world, BlockPos pos, BlockState state, boolean left, boolean top, boolean down)
    {
        int index = state.get(INDEX);
        if (!top && index == 2) return false;
        if (top && index != 2) return false;
        if (!down && index == 0) return false;
        if (down && index != 0) return false;
        Direction facing = state.get(FACING);
        for (final Direction face : Direction.Plane.HORIZONTAL)
        {
            if ((left && face == facing.rotateYCCW()) || (!left && face == facing.rotateY()))
            {
                BlockState sideState = world.getBlockState(pos.offset(face));
                Block block = sideState.getBlock();
                return sideState.isSolid() || block instanceof BlockElectricGate || block instanceof BlockBasicElectricFence;
            }
        }
        return false;
    }

    private boolean isCore(BlockState state)
    {
        int index = state.get(INDEX);
        return index == 1;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    return eState.with(CORE, isCore(state))
        //            .with(ACTIVE_LEFT, ActiveSide(world, pos, state, true, false, false))
        //            .with(ACTIVE_RIGHT, ActiveSide(world, pos, state, false, false, false))
        //            .with(ACTIVE_LEFT_TOP, ActiveSide(world, pos, state, true, true, false))
        //            .with(ACTIVE_RIGHT_TOP, ActiveSide(world, pos, state, false, true, false))
        //            .with(ACTIVE_LEFT_DOWN, ActiveSide(world, pos, state, true, false, true))
        //            .with(ACTIVE_RIGHT_DOWN, ActiveSide(world, pos, state, false, false, true));
        //}
        return state;
    }
}
