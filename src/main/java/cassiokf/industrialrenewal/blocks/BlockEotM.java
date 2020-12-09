package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockEotM extends BlockHorizontalFacing
{
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 1, 3, 1, 15, 13);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(15, 1, 3, 16, 15, 13);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(3, 1, 15, 13, 15, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(3, 1, 1, 13, 15, 0);

    public BlockEotM()
    {
        super(Block.Properties.create(Material.WOOD));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction face;
        if (Direction.Plane.HORIZONTAL.test(context.getFace()))
        {
            face = context.getFace().getOpposite();
        } else
        {
            face = context.getPlacementHorizontalFacing();
        }
        return getDefaultState().with(FACING, face);
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        switch (state.get(FACING))
        {
            default:
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
        }
    }
}
