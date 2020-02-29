package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockBufferStopRail extends BlockAbstractHorizontalFacing
{
    protected static final VoxelShape FLAT_AABB = Block.makeCuboidShape(0, 0, 0, 16, 10, 16);

    public BlockBufferStopRail()
    {
        super(Block.Properties.create(Material.IRON));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return FLAT_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return FLAT_AABB;
    }
}