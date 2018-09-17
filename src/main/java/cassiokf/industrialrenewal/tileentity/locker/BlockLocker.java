package cassiokf.industrialrenewal.tileentity.locker;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
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
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLocker extends BlockTileEntity<TileEntityLocker> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public BlockLocker(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (playerIn.isSneaking()) {
            worldIn.setBlockState(pos, state.cycleProperty(OPEN));
            return true;
        }
        ILockableContainer ilockablecontainer = getContainer(worldIn, pos);
        if (ilockablecontainer != null) {
            playerIn.displayGUIChest(ilockablecontainer);
            playerIn.addStat(StatList.CHEST_OPENED);
        }
        return true;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
    }

    @Nullable
    public ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
        return this.getContainer(worldIn, pos);
    }

    @Nullable
    public ILockableContainer getContainer(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityLocker)) {
            return null;
        }
        return (TileEntityLocker) tileentity;
    }

    private boolean connectDown(IBlockAccess world, BlockPos pos) {
        IBlockState downState = world.getBlockState(pos.down());
        return downState.getBlock() instanceof BlockLocker;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        return state.withProperty(DOWN, connectDown(world, pos));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(OPEN, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(OPEN)) {
            i |= 4;
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(OPEN, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OPEN, DOWN);
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

    @Override
    public Class<TileEntityLocker> getTileEntityClass() {
        return TileEntityLocker.class;
    }

    @Nullable
    @Override
    public TileEntityLocker createTileEntity(World world, IBlockState state) {
        return new TileEntityLocker();
    }
}
