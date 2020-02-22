package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockFuseBoxConduitExtension extends BlockAbstractHorizontalFacing
{
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 0, 6, 2, 16, 10);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(14, 0, 6, 16, 16, 10);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(6, 0, 14, 10, 16, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(6, 0, 0, 10, 16, 2);

    public BlockFuseBoxConduitExtension()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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
