package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityGauge;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockGauge extends BlockTileEntity<TileEntityGauge>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final IProperty<EnumFacing> BASE = PropertyDirection.create("base");

    public BlockGauge(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.NORTH));

    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        TileEntityGauge te = (TileEntityGauge) world.getTileEntity(pos);
        return state.withProperty(BASE, te.getBaseFacing());
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        super.onNeighborChange(world, pos, neighbor);
        TileEntityGauge te = (TileEntityGauge) world.getTileEntity(pos);
        if (te != null) te.forceCheck();
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        boolean rotated = super.rotateBlock(world, pos, axis);
        TileEntityGauge te = (TileEntityGauge) world.getTileEntity(pos);
        if (te != null && world.isRemote) te.forceIndicatorCheck();
        return rotated;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, BASE);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(BASE, facing.getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntityGauge te = (TileEntityGauge) worldIn.getTileEntity(pos);
        EnumFacing facing = state.getValue(BASE);
        te.setBaseFacing(facing);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Class<TileEntityGauge> getTileEntityClass()
    {
        return TileEntityGauge.class;
    }

    @Nullable
    @Override
    public TileEntityGauge createTileEntity(World world, IBlockState state)
    {
        return new TileEntityGauge();
    }
}