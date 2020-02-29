package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class Block3x3x3Base<TE extends TileEntity3x3MachineBase> extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public Block3x3x3Base(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.offset(state.get(FACING)).up(), state.with(MASTER, true));
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            for (int y = -1; y < 2; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    for (int x = -1; x < 2; x++)
                    {
                        BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        if (y != 0 || z != 0 || x != 0)
                            worldIn.setBlockState(currentPos, state.with(MASTER, false));
                    }
                }
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TE te = (TE) worldIn.getTileEntity(pos);
        if (te != null)
        {
            te.breakMultiBlocks();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        PlayerEntity player = worldIn.getDimension().getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        for (int y = 0; y < 3; y++)
        {
            for (int z = 0; z < 3; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    Direction facing = player.getHorizontalFacing();
                    BlockPos currentPos = new BlockPos(pos.offset(facing, z).offset(facing.rotateY(), x).offset(Direction.UP, y));
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.getMaterial().isReplaceable()) return false;
                }
            }
        }
        return true;
    }

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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(MASTER, false);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
