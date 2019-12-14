package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityValvePipeLarge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockValvePipeLarge extends BlockToggleableBase<TileEntityValvePipeLarge>
{

    public BlockValvePipeLarge(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public Class<TileEntityValvePipeLarge> getTileEntityClass()
    {
        return TileEntityValvePipeLarge.class;
    }

    @Nullable
    @Override
    public TileEntityValvePipeLarge createTileEntity(World world, IBlockState state) {
        return new TileEntityValvePipeLarge();
    }
}