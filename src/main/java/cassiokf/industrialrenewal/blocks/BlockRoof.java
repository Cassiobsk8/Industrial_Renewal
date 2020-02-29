package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockRoof extends BlockAbstractHorizontalFacing
{
    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(0, 12, 0, 16, 16, 16);
    protected static final VoxelShape BOT_AABB = Block.makeCuboidShape(0, 0, 0, 16, 14, 16);

    public BlockRoof()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 255;
    }

    private Boolean isEven(BlockPos pos, boolean baseOnX)
    {
        int number = pos.getZ();
        if (baseOnX) number = pos.getX();
        return (number % 2) == 0;
    }

    private boolean canConnectTo(final IBlockReader world, final BlockPos ownPos, final Direction ownFacing, final Direction neighborDirection)
    {
        final BlockPos neighborPos = ownPos.offset(neighborDirection);
        final BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborDirection == Direction.DOWN) return neighborState.isSolid();

        if (((neighborDirection == Direction.EAST || neighborDirection == Direction.WEST) && (ownFacing == Direction.SOUTH || ownFacing == Direction.NORTH))
                || ((neighborDirection == Direction.SOUTH || neighborDirection == Direction.NORTH) && (ownFacing == Direction.WEST || ownFacing == Direction.EAST)))
        {
            BlockState sState = world.getBlockState(ownPos.offset(Direction.SOUTH));
            BlockState nState = world.getBlockState(ownPos.offset(Direction.NORTH));
            Block sBlock = sState.getBlock();
            Block nBlock = nState.getBlock();
            if ((sBlock instanceof BlockRoof || sBlock instanceof BlockCatwalkLadder || sState.isSolid()) && (nBlock instanceof BlockRoof || nBlock instanceof BlockCatwalkLadder || nState.isSolid()))
            {
                // (block pos is Even) && (neighbor SW)
                boolean invert = ownFacing == Direction.WEST || ownFacing == Direction.EAST;
                Block nb = neighborState.getBlock();
                return isEven(ownPos, invert)
                        && (nb instanceof BlockRoof || neighborState.isSolid() || nb instanceof BlockPillar || nb instanceof BlockColumn);
            }
        }
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return BASE_AABB;
    }
}
