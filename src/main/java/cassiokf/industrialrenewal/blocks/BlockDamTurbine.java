package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.Block3x3Top1Base;
import cassiokf.industrialrenewal.tileentity.TileEntityDamTurbine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDamTurbine extends Block3x3Top1Base<TileEntityDamTurbine>
{
    public BlockDamTurbine(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityDamTurbine createTileEntity(World world, IBlockState state)
    {
        return new TileEntityDamTurbine();
    }
}
