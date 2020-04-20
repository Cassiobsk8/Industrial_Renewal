package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTileEntityConnectedMultiblocks<TE extends TileEntityMultiBlocksTube> extends BlockTileEntityConnected<TE>
{

    public BlockTileEntityConnectedMultiblocks(Material material, String name, CreativeTabs tab)
    {
        super(material, name, tab);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if (!worldIn.isRemote)
        {
            TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) worldIn.getTileEntity(pos);
            if (te != null) te.checkForOutPuts(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMultiBlocksTube)
        {
            ((TileEntityMultiBlocksTube) te).startBreaking();
            for (EnumFacing face : EnumFacing.VALUES)
            {
                TileEntity tile = worldIn.getTileEntity(pos.offset(face));
                if (tile != null) ((TileEntityMultiBlocksTube) te).removeMachine(tile);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
}
