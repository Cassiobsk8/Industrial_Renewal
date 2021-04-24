package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockMultiBlockBase<TE extends TileEntityMultiBlockBase> extends BlockTEHorizontalFacing<TE>
{
    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public BlockMultiBlockBase(Block.Properties properties)
    {
        super(properties.sound(SoundType.METAL).hardnessAndResistance(12,30));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(getMasterPosBasedOnPlace(pos, state.get(FACING)), state.with(MASTER, true));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            Direction facing = state.get(FACING);
            List<BlockPos> posList = getMachineBlockPosList(pos, facing);
            for (BlockPos currentPos : posList)
            {
                if (!currentPos.equals(pos))
                    worldIn.setBlockState(currentPos, state.with(MASTER, false));
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMultiBlockBase)
        {
            ((TileEntityMultiBlockBase) te).breakMultiBlocks();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        World world = worldIn.getDimension().getWorld();
        PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        Direction facing = player.getHorizontalFacing();
        List<BlockPos> posList = getMachineBlockPosList(getMasterPosBasedOnPlace(pos, facing), facing);
        for (BlockPos currentPos : posList)
        {
            if (!isReplaceable(world, currentPos)) return false;
        }
        return true;
    }

    protected BlockPos getMasterPosBasedOnPlace(BlockPos pos, Direction facing)
    {
        return pos.up();
    }

    public abstract List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing);

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(MASTER, false);
    }
}
