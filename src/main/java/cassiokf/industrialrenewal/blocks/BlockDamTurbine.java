package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityDamTurbine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDamTurbine extends Block3x3Top1Base<TileEntityDamTurbine>
{
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);

    public BlockDamTurbine(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public Class<TileEntityDamTurbine> getTileEntityClass()
    {
        return TileEntityDamTurbine.class;
    }

    @Nullable
    @Override
    public TileEntityDamTurbine createTileEntity(World world, IBlockState state)
    {
        return new TileEntityDamTurbine();
    }
}
