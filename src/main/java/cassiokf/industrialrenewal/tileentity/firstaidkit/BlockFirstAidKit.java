package cassiokf.industrialrenewal.tileentity.firstaidkit;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.network.PacketUpdateFirstAidKit;
import cassiokf.industrialrenewal.util.GUIHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockFirstAidKit extends BlockTileEntity<TileEntityFirstAidKit> {

    public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.1875F, 0.1875F, 0.3125F, 0.8125F, 0.8125F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.1875F, 0.1875F, 0.6875F, 0.8125F, 0.8125F);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.1875F, 0.1875F, 0.6875F, 0.8125F, 0.8125F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.1875F, 0.1875F, 0.3125F, 0.8125F, 0.8125F, 0);


    public BlockFirstAidKit(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing dir = state.getValue(FACING);
        switch (dir) {
            default:
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        if (!player.isSneaking() && player.inventory.getCurrentItem() == ItemStack.EMPTY) {
            ItemStack stack = itemInKit(world, pos);
            if (stack != null) {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 150, 1, false, false));
                stack.shrink(1);
                TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(pos);
                IndustrialRenewal.network.sendToAllAround(new PacketUpdateFirstAidKit(te), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
                te.markDirty();
            }
            return true;
        }
        if (player.isSneaking()) {
            OpenGUI(world, pos, player);
            return true;
        }
        return false;
    }

    private ItemStack itemInKit(World world, BlockPos pos) {
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(pos);
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return null;
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player) {
        player.openGui(IndustrialRenewal.instance, GUIHandler.FIRSTAIDKIT, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(pos);
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

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
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

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
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
        return BlockFaceShape.UNDEFINED;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public Class<TileEntityFirstAidKit> getTileEntityClass() {
        return TileEntityFirstAidKit.class;
    }

    @Nullable
    @Override
    public TileEntityFirstAidKit createTileEntity(World world, IBlockState state) {
        return new TileEntityFirstAidKit();
    }
}