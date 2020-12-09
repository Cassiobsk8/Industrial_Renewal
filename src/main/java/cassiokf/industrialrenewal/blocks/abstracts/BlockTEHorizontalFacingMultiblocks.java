package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTEHorizontalFacingMultiblocks<TE extends TileEntityMultiBlocksTube> extends BlockTEHorizontalFacing<TE>
{

    public BlockTEHorizontalFacingMultiblocks(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isRemote
                && !(blockIn instanceof RedstoneWireBlock)
                && !(blockIn instanceof RedstoneDiodeBlock)
                && !(blockIn instanceof RedstoneTorchBlock))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityMultiBlocksTube) ((TileEntityMultiBlocksTube) te).checkForOutPuts();
            if (IRConfig.Main.debugMessages.get())
                System.out.println("Checking for outputs caused by " + blockIn + " " + fromPos);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMultiBlocksTube)
        {
            ((TileEntityMultiBlocksTube) te).startBreaking();
            for (Direction face : Direction.values())
            {
                TileEntity tile = worldIn.getTileEntity(pos.offset(face));
                if (tile != null) ((TileEntityMultiBlocksTube) te).removeMachine(tile);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
