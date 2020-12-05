package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityTrash;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTrash extends BlockHorizontalFacing
{
    public BlockTrash(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityTrash createTileEntity(World world,  BlockState state)
    {
        return new TileEntityTrash();
    }
}
