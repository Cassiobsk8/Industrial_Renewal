package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntitySteamTurbine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSteamTurbine extends Block3x3x3Base<TileEntitySteamTurbine>
{
    public BlockSteamTurbine(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public Class<TileEntitySteamTurbine> getTileEntityClass()
    {
        return TileEntitySteamTurbine.class;
    }

    @Nullable
    @Override
    public TileEntitySteamTurbine createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySteamTurbine();
    }
}
