package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityEnergyLevel;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEnergyLevel extends BlockHorizontalFacing
{
    public static final IProperty<EnumFacing> BASE = PropertyDirection.create("base");

    public BlockEnergyLevel(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setHardness(0.8f);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.NORTH));

    }

    @Nonnull
    @Override
    public  BlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) world.getTileEntity(pos);
        if (te != null) return state.withProperty(BASE, te.getBaseFacing());
        return state;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, BASE);
    }

    @Nonnull
    @Override
    public  BlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(BASE, facing.getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos,  BlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        EnumFacing facing = state.getValue(BASE);
        if (te instanceof TileEntityEnergyLevel) ((TileEntityEnergyLevel) te).setBaseFacing(facing);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        super.onNeighborChange(world, pos, neighbor);
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) world.getTileEntity(pos);
        if (te != null) te.forceCheck();
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        boolean rotated = super.rotateBlock(world, pos, axis);
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) world.getTileEntity(pos);
        if (te != null && world.isRemote) te.forceIndicatorCheck();
        return rotated;
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityEnergyLevel createTileEntity(World world,  BlockState state)
    {
        return new TileEntityEnergyLevel();
    }
}