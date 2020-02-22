package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTileEntityConnectedMultiblocks<TE extends TileEntityMultiBlocksTube> extends BlockTileEntityConnected<TE>
{

    public BlockTileEntityConnectedMultiblocks(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isRemote)
        {
            TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) worldIn.getTileEntity(pos);
            if (te != null) te.checkForOutPuts(pos);
        }
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
}
