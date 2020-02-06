package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityEnergySwitch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEnergySwitch extends BlockToggleableBase<TileEntityEnergySwitch>
{
    public BlockEnergySwitch(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public Class<TileEntityEnergySwitch> getTileEntityClass()
    {
        return TileEntityEnergySwitch.class;
    }

    @Override
    public void spawnPartivle(World world, double i, double j, double k)
    {
    }

    @Nullable
    @Override
    public TileEntityEnergySwitch createTileEntity(World world, IBlockState state)
    {
        return new TileEntityEnergySwitch();
    }
}