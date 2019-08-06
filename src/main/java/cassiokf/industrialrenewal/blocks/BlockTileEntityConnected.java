package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public abstract class BlockTileEntityConnected<TE extends TileEntity> extends BlockTileEntity<TE>
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final IUnlistedProperty<Boolean> SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("south"));
    public static final IUnlistedProperty<Boolean> NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("north"));
    public static final IUnlistedProperty<Boolean> EAST = new Properties.PropertyAdapter<>(PropertyBool.create("east"));
    public static final IUnlistedProperty<Boolean> WEST = new Properties.PropertyAdapter<>(PropertyBool.create("west"));
    public static final IUnlistedProperty<Boolean> UP = new Properties.PropertyAdapter<>(PropertyBool.create("up"));
    public static final IUnlistedProperty<Boolean> DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("down"));

    public BlockTileEntityConnected(Material material, String name, CreativeTabs tab)
    {
        super(material, name, tab);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{SOUTH, NORTH, EAST, WEST, UP, DOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
