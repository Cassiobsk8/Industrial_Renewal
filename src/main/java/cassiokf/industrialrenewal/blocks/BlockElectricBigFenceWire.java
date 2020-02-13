package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockElectricBigFenceWire extends BlockBasicElectricFence
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 2);

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB CBASE_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);

    public BlockElectricBigFenceWire(Block.Properties properties)
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

    private boolean IsBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceWire;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up()).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up(2)).getMaterial().isReplaceable();
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            return BASE_AABB;
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            if (!isActualState) {
                state = state.getActualState(worldIn, pos);
            }
            addCollisionBoxToList(pos, entityBox, collidingBoxes, CBASE_AABB);
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, INDEX);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(INDEX, 0);
    }

}
