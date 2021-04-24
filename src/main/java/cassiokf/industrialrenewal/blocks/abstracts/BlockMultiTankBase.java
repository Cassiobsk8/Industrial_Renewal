package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockMultiTankBase<T extends TEMultiTankBase> extends BlockMultiBlockBase<T>
{
    public static final IntegerProperty TOP = IntegerProperty.create("top", 0, 2);
    public static final IntegerProperty DOWN = IntegerProperty.create("bot", 0, 2);

    public BlockMultiTankBase(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, TOP, DOWN);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        boolean isMaster = stateIn.get(MASTER);
        return stateIn.with(TOP, (isMaster ? isTop(worldIn, currentPos) : 0))
                .with(DOWN, (isMaster ? isBot(worldIn, currentPos) : 0));
    }

    private int isTop(IWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos.offset(Direction.UP, 3));
        boolean thereIsOnTop = instanceOf(state.getBlock()) && state.get(MASTER);
        return thereIsOnTop ? 2 : 1;
    }

    protected int isBot(IWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos.offset(Direction.DOWN, 3));
        boolean thereIsOnBottom = instanceOf(state.getBlock()) && state.get(MASTER);
        return thereIsOnBottom ? 2 : 1;
    }

    public abstract boolean instanceOf(Block block);

    @Nullable
    @Override
    public abstract T createTileEntity(BlockState state, IBlockReader world);
}
