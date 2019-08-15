package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityConcrete;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockConcrete extends BlockTileEntity<TileEntityConcrete>
{
    public BlockConcrete(Material material, String name, CreativeTabs tab)
    {
        super(material, name, tab);
    }

    @Override
    public Class<TileEntityConcrete> getTileEntityClass()
    {
        return TileEntityConcrete.class;
    }

    @Nullable
    @Override
    public TileEntityConcrete createTileEntity(World world, IBlockState state)
    {
        return new TileEntityConcrete();
    }
}
