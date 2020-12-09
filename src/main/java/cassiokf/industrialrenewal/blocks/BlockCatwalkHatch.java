package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalActivate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class BlockCatwalkHatch extends BlockAbstractHorizontalActivate
{
    protected static final VoxelShape RDOWN_AABB = Block.makeCuboidShape(0, 0, 0, 16, 3, 16);

    protected static final VoxelShape OPEN_NORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape OPEN_SOUTH_AABB = Block.makeCuboidShape(0, 0, 14, 16, 16, 16);
    protected static final VoxelShape OPEN_WEST_AABB = Block.makeCuboidShape(0, 0, 0, 2, 16, 16);
    protected static final VoxelShape OPEN_EAST_AABB = Block.makeCuboidShape(14, 0, 0, 16, 16, 16);

    public BlockCatwalkHatch()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return state.get(ACTIVE);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        if (state.get(ACTIVE))
        {
            return 0;
        } else
        {
            return 250;
        }
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return world.getBlockState(pos).get(ACTIVE);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        if (!collision) return RDOWN_AABB;
        if (state.get(ACTIVE))
        {
            Direction direction = state.get(FACING);
            switch (direction)
            {
                default:
                case NORTH:
                    return OPEN_NORTH_AABB;
                case SOUTH:
                    return OPEN_SOUTH_AABB;
                case WEST:
                    return OPEN_WEST_AABB;
                case EAST:
                    return OPEN_EAST_AABB;
            }
        } else
        {
            return RDOWN_AABB;
        }
    }
}
