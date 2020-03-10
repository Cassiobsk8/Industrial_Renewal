package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockBasicContainer;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidLoader extends BlockBasicContainer<TileEntityFluidLoader>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger UNLOAD = PropertyInteger.create("unload", 0, 2);
    public static final PropertyBool MASTER = PropertyBool.create("master");

    public BlockFluidLoader(String name, CreativeTabs tab) {
        super(name, tab, Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            if (state.getValue(MASTER))
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityFluidLoader)
                {
                    OpenGUI(worldIn, pos, playerIn);
                }
            } else
            {
                TileEntity tileentity = worldIn.getTileEntity(pos.down());
                if (tileentity instanceof TileEntityFluidLoader)
                {
                    OpenGUI(worldIn, pos.down(), playerIn);
                }
            }
        }
        return true;
    }

    public void OpenGUI(World world, BlockPos pos, EntityPlayer player)
    {
        player.openGui(IndustrialRenewal.instance, GUIHandler.FLUIDLOADER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            worldIn.setBlockState(pos.up(), state.withProperty(MASTER, false));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            if (IsLoader(worldIn, pos.up())) worldIn.setBlockToAir(pos.up());
        } else
        {
            if (IsLoader(worldIn, pos.down())) worldIn.setBlockToAir(pos.down());
        }
        super.breakBlock(worldIn, pos, state);
    }

    private boolean IsLoader(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockFluidLoader;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
                && worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up());
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        return state.withProperty(UNLOAD, isUnload(world, pos, state));
    }

    private int isUnload(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        if (!state.getValue(MASTER)) return 0;
        TileEntityFluidLoader te = (TileEntityFluidLoader) world.getTileEntity(pos);
        if (te != null && te.isUnload()) return 2;
        return 1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(MASTER, true);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, UNLOAD, MASTER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta)
    {
        int directionIndex = meta;
        if (meta > 3) directionIndex -= 4;
        boolean index = true;
        if (meta > 3) index = false;
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(directionIndex)).withProperty(MASTER, index);
    }

    @Override
    public int getMetaFromState(final IBlockState state)
    {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (!state.getValue(MASTER)) i += 4;
        return i;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public Class<TileEntityFluidLoader> getTileEntityClass() {
        return TileEntityFluidLoader.class;
    }

    @Nullable
    @Override
    public TileEntityFluidLoader createTileEntity(World world, IBlockState state) {
        return new TileEntityFluidLoader();
    }
}
