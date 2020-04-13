package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

public abstract class BlockTileEntityConnected<TE extends TileEntity> extends BlockHorizontalFacing
{
    public static final IUnlistedProperty<Boolean> SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("south"));
    public static final IUnlistedProperty<Boolean> NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("north"));
    public static final IUnlistedProperty<Boolean> EAST = new Properties.PropertyAdapter<>(PropertyBool.create("east"));
    public static final IUnlistedProperty<Boolean> WEST = new Properties.PropertyAdapter<>(PropertyBool.create("west"));
    public static final IUnlistedProperty<Boolean> UP = new Properties.PropertyAdapter<>(PropertyBool.create("up"));
    public static final IUnlistedProperty<Boolean> DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("down"));

    public BlockTileEntityConnected(Material material, String name, CreativeTabs tab)
    {
        super(name, tab, material);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{SOUTH, NORTH, EAST, WEST, UP, DOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TEBase) ((TEBase) te).onBlockBreak();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);
}
