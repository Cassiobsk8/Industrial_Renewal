package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public abstract class BlockConnectedMultiblocks<TE extends TileEntityMultiBlocksTube> extends BlockAbstractHorizontalFacing
{

    public BlockConnectedMultiblocks(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) worldIn.getTileEntity(pos);
        if (te != null) te.checkForOutPuts(pos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) worldIn.getTileEntity(currentPos);
        if (te != null) te.requestRefresh();
        return stateIn;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) worldIn.getTileEntity(pos);
        if (te != null)
        {
            for (Direction face : Direction.values())
            {
                BlockPos posM = pos.offset(face);
                if (te.getMaster() != null) te.getMaster().removeMachine(pos, posM);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public abstract TE createTileEntity(BlockState state, IBlockReader world);

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_)
    {
    }
}
