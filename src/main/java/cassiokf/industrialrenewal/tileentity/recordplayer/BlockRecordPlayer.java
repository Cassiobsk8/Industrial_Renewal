package cassiokf.industrialrenewal.tileentity.recordplayer;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.util.GUIHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockNote;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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

public class BlockRecordPlayer extends BlockTileEntity<TileEntityRecordPlayer> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool DOWNNOTEBLOCK = PropertyBool.create("downnoteblock");
    public static final PropertyBool DISK0 = PropertyBool.create("disc0");
    public static final PropertyBool DISK1 = PropertyBool.create("disc1");
    public static final PropertyBool DISK2 = PropertyBool.create("disc2");
    public static final PropertyBool DISK3 = PropertyBool.create("disc3");

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.875D, 1.0D);

    public BlockRecordPlayer(String name, CreativeTabs tab) {
        super(Material.CIRCUITS, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DISK0, false).withProperty(DISK1, false)
                .withProperty(DISK2, false).withProperty(DISK3, false).withProperty(DOWNNOTEBLOCK, false));
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
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                world.spawnEntity(item);
            }
        }

        super.breakBlock(world, pos, state);
        //world.removeTileEntity(pos);
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player) {
        player.openGui(IndustrialRenewal.instance, GUIHandler.RECORDPLAYER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean isDownBlockaNoteBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() instanceof BlockNote;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(DOWNNOTEBLOCK, isDownBlockaNoteBlock(world, pos));
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(DOWNNOTEBLOCK, isDownBlockaNoteBlock(worldIn, pos));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, DOWNNOTEBLOCK, DISK0, DISK1, DISK2, DISK3);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        return i;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.DOWN) return BlockFaceShape.SOLID;
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Class<TileEntityRecordPlayer> getTileEntityClass() {
        return TileEntityRecordPlayer.class;
    }

    @Nullable
    @Override
    public TileEntityRecordPlayer createTileEntity(World world, IBlockState state) {
        return new TileEntityRecordPlayer();
    }
}
