package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockNote;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockRecordPlayer extends BlockHorizontalFacing
{
    public static final PropertyBool DOWNNOTEBLOCK = PropertyBool.create("downnoteblock");

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.875D, 1.0D);

    public BlockRecordPlayer(String name, CreativeTabs tab)
    {
        super(name, tab, Material.WOOD);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DOWNNOTEBLOCK, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            OpenGUI(world, pos, player);
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityRecordPlayer te = (TileEntityRecordPlayer) world.getTileEntity(pos);
        assert te != null;
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        assert inventory != null;
        Utils.dropInventoryItems(world, pos, inventory);
        super.breakBlock(world, pos, state);
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player) {
        player.openGui(IndustrialRenewal.instance, GUIHandler.RECORDPLAYER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean isDownBlockaNoteBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() instanceof BlockNote;
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        return state.withProperty(DOWNNOTEBLOCK, isDownBlockaNoteBlock(world, pos));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(DOWNNOTEBLOCK, isDownBlockaNoteBlock(worldIn, pos));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, DOWNNOTEBLOCK);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.DOWN) return BlockFaceShape.SOLID;
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityRecordPlayer createTileEntity(World world, IBlockState state) {
        return new TileEntityRecordPlayer();
    }
}
