package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockScaffold extends BlockAbstractSixWayConnections
{
    protected static final VoxelShape CBASE_AABB = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);

    public BlockScaffold()
    {
        super(Block.Properties.create(Material.IRON), 16, 16);
        BlockState state = getDefaultState();
        for (Direction direction : Direction.values())
        {
            state = state.with(getPropertyBasedOnDirection(direction), true);
        }
        setDefaultState(state);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.getHeldItemMainhand().getItem().equals(BlocksRegistration.SCAFFOLD_ITEM.get()))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockScaffold)
            {
                n++;
            }
            if (worldIn.getBlockState(pos.up(n)).getBlock().isAir(worldIn.getBlockState(pos.up(n)), worldIn, pos.up(n)))
            {
                worldIn.setBlockState(pos.up(n), BlocksRegistration.SCAFFOLD.get().getDefaultState());
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);

                if (!player.isCreative())
                {
                    player.getHeldItemMainhand().shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    protected boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        Block nbd = world.getBlockState(ownPos.offset(neighborDirection).down()).getBlock();
        BlockState upState = world.getBlockState(ownPos.up());
        if (neighborDirection == Direction.DOWN)
        {
            return neighborState.isSolidSide(world, ownPos.down(), Direction.UP);
        }
        if (neighborDirection != Direction.UP)
        {
            return !(upState.getBlock() instanceof BlockScaffold)
                    && !(upState.isSolidSide(world, ownPos.up(), Direction.DOWN))
                    && !(nb instanceof BlockScaffold)
                    && !(nbd instanceof BlockScaffold);
        }
        return !neighborState.isSolidSide(world, ownPos.up(), Direction.DOWN) || nb instanceof BlockScaffold;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.DOWN)
        {
            if (!isValidPosition(stateIn, worldIn, currentPos))
            {
                return Blocks.AIR.getDefaultState(); //Its automatic drops loot_table drop
            }
        }
        if (facing != Direction.UP)
            return stateIn.with(getPropertyBasedOnDirection(facing), canConnectTo(worldIn, currentPos, facing));
        for (Direction direction : Direction.values())
        {
            stateIn = stateIn.with(getPropertyBasedOnDirection(direction), canConnectTo(worldIn, currentPos, direction));
        }
        return stateIn;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockState downState = worldIn.getBlockState(pos.down());
        return downState.isSolidSide(worldIn, pos.down(), Direction.UP)
                || downState.getBlock() instanceof BlockScaffold;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        return isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return FULL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return CBASE_AABB;
    }
}
