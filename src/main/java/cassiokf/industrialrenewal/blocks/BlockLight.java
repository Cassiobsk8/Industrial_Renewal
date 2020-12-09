package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockLight extends BlockAbstractFacing
{
    protected static final VoxelShape NORTH_AABB = makeCuboidShape(5, 3, 0, 11, 13, 5);
    protected static final VoxelShape SOUTH_AABB = makeCuboidShape(5, 3, 11, 11, 13, 16);
    protected static final VoxelShape EAST_AABB = makeCuboidShape(11, 3, 5, 16, 13, 11);
    protected static final VoxelShape WEST_AABB = makeCuboidShape(0, 3, 5, 5, 13, 11);
    protected static final VoxelShape UP_AABB = makeCuboidShape(5, 11, 3, 11, 16, 13);
    protected static final VoxelShape DOWN_AABB = makeCuboidShape(5, 0, 3, 11, 5, 13);

    public BlockLight()
    {
        super(Block.Properties.create(Material.IRON).lightValue(15));
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        Direction dir = state.get(FACING);
        switch (dir)
        {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case DOWN:
                return DOWN_AABB;
            default:
                return UP_AABB;
        }
    }
}
