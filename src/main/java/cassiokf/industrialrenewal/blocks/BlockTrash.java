package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityTrash;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockTrash extends BlockTileEntity<TileEntityTrash>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);

    public BlockTrash(Block.Properties properties)
    {
        super(properties);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
        {
            return FULL_AABB;
        }
    */
    @Nullable
    @Override
    public TileEntityTrash createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTrash();
    }
}
