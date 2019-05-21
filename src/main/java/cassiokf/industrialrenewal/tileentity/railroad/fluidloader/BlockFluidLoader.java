package cassiokf.industrialrenewal.tileentity.railroad.fluidloader;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.Registry.GUIHandler;
import cassiokf.industrialrenewal.Registry.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnFluidLoader;
import cassiokf.industrialrenewal.tileentity.railroad.railloader.BlockLoaderRail;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidLoader extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool UNLOAD = PropertyBool.create("unload");
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);
    protected String name;

    public BlockFluidLoader(String name, CreativeTabs tab) {
        super(Material.IRON);
        this.name = name;
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        setRegistryName(References.MODID, name);
        setUnlocalizedName(References.MODID + "." + name);
        setCreativeTab(tab);
        setHardness(2f);
        setResistance(5f);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (worldIn.isRemote) {
            if (tileentity instanceof TileEntityFluidLoader) {
                ((TileEntityFluidLoader) tileentity).onChange();
            }
            return true;
        } else {
            if (tileentity instanceof TileEntityFluidLoader) {
                OpenGUI(worldIn, pos, playerIn);
                playerIn.addStat(StatList.HOPPER_INSPECTED);
            }
            return true;
        }
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player) {
        OpenEvent(world, pos);
        player.openGui(IndustrialRenewal.instance, GUIHandler.FLUIDLOADER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void OpenEvent(World world, BlockPos pos) {
        if (world.isRemote) {
            TileEntityFluidLoader te = (TileEntityFluidLoader) world.getTileEntity(pos);
            if (te != null) {
                NetworkHandler.INSTANCE.sendToServer(new PacketReturnFluidLoader(te));
            }
        }
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, UNLOAD);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
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

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public Class<TileEntityFluidLoader> getTileEntityClass() {
        return TileEntityFluidLoader.class;
    }

    @Nullable
    @Override
    public TileEntityFluidLoader createTileEntity(World world, IBlockState state) {
        return new TileEntityFluidLoader();
    }

    public void registerItemModel(Item itemBlock) {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidLoader();
    }
}
