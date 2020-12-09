package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockToggleableBase;
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

    @Nullable
    @Override
    public TileEntityEnergySwitch createTileEntity(World world, IBlockState state)
    {
        return new TileEntityEnergySwitch();
    }
}