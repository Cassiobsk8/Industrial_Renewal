package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockAbstractBigFence extends BlockBasicElectricFence
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 2);

    public BlockAbstractBigFence(Properties property)
    {
        super(property, 16);
    }

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
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        return false;
    }

    @Override
    public BlockState getDefaultBlockState()
    {
        return getDefaultState();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        switch (state.get(INDEX))
        {
            case 0:
                if (isBigFence(worldIn, pos.up())) worldIn.removeBlock(pos.up(), false);
                if (isBigFence(worldIn, pos.up(2))) worldIn.removeBlock(pos.up(2), false);
                break;
            case 1:
                if (isBigFence(worldIn, pos.down())) worldIn.removeBlock(pos.down(), false);
                if (isBigFence(worldIn, pos.up())) worldIn.removeBlock(pos.up(), false);
                break;
            case 2:
                if (isBigFence(worldIn, pos.down())) worldIn.removeBlock(pos.down(), false);
                if (isBigFence(worldIn, pos.down(2))) worldIn.removeBlock(pos.down(2), false);
                break;
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);

    }

    public abstract boolean isBigFence(World world, BlockPos pos);

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up()).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up(2)).getMaterial().isReplaceable();
    }

}
