package cassiokf.industrialrenewal.tileentity.railroad.fluidloader;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.blocks.BlockBasicContainer;
import cassiokf.industrialrenewal.tileentity.railroad.railloader.BlockLoaderRail;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidLoader extends BlockBasicContainer<TileEntityFluidLoader>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool UNLOAD = PropertyBool.create("unload");
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);

    public BlockFluidLoader(String name, CreativeTabs tab) {
        super(name, tab, Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityFluidLoader) {
                OpenGUI(worldIn, pos, playerIn);
                playerIn.addStat(StatList.HOPPER_INSPECTED);
            }
        }
        return true;
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player) {
        player.openGui(IndustrialRenewal.instance, GUIHandler.FLUIDLOADER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean isUnload(IBlockAccess world, BlockPos pos) {
        IBlockState downState = world.getBlockState(pos.down(2));
        return !(downState.getBlock() instanceof BlockLoaderRail);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        return state.withProperty(UNLOAD, isUnload(world, pos));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, UNLOAD);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.UP ? BlockFaceShape.BOWL : BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_AABB;
    }

    public Class<TileEntityFluidLoader> getTileEntityClass() {
        return TileEntityFluidLoader.class;
    }

    @Nullable
    @Override
    public TileEntityFluidLoader createTileEntity(World world, IBlockState state) {
        return new TileEntityFluidLoader();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidLoader();
    }
}
