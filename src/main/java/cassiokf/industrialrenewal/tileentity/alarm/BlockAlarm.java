package cassiokf.industrialrenewal.tileentity.alarm;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


public class BlockAlarm extends BlockTileEntity<TileEntityAlarm> {

    private static final AxisAlignedBB UP_BLOCK_AABB = new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F);
    private static final AxisAlignedBB DOWN_BLOCK_AABB = new AxisAlignedBB(0.125F, 1, 0.125F, 0.875F, 0.5625F, 0.875F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.125F, 0.5625F, 0.875F, 0.875F);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0);
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    private final long PERIOD = 1000L; // Adjust to suit timing
    private long lastTime = System.currentTimeMillis() - PERIOD;


    public BlockAlarm(String name) {
        super(Material.IRON, name);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        setCreativeTab(IndustrialRenewal.creativeTab);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.UP));

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing dir = state.getValue(FACING);
        switch (dir) {
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
            case DOWN:
                return DOWN_BLOCK_AABB;
            default:
                return UP_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileEntityAlarm tileEntity = getTileEntity(world, pos);
        world.playSound((EntityPlayer) null,pos.add(0.5, 0.5, 0.5), net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:modern_alarm")), SoundCategory.BLOCKS, 2.0F, 1.0F);
        tileEntity.checkPowered(world, tileEntity);
        return true;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (!(world.isSideSolid(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()), world.getBlockState(pos).getValue(FACING).getOpposite(), true))) {
            this.dropBlockAsItem((World) world, pos, world.getBlockState(pos), 0);
            ((World) world).setBlockToAir(pos);
        }
    }
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        final TileEntityAlarm tileEntity = getTileEntity(world, pos);
        if (!(world.isSideSolid(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()), world.getBlockState(pos).getValue(FACING).getOpposite()))) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
        if (tileEntity.checkPowered(world, tileEntity)) {
            long thisTime = System.currentTimeMillis();
            if ((thisTime - lastTime) >= PERIOD) {
                lastTime = thisTime;
                world.playSound((EntityPlayer) null, pos.add(0.5, 0.5, 0.5), net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:modern_alarm")), SoundCategory.BLOCKS, 2.0F, 1.0F);
            }
        }
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (world.isSideSolid(pos.offset(facing.getOpposite()), facing.getOpposite())) {
            return this.getDefaultState().withProperty(FACING, facing);
        } else {
            if (world.getBlockState(pos.down()).getBlock() != Blocks.AIR) {
                return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
            } else {
                return null;
            }
        }
    }
    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {

        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }
    public EnumFacing getFacing(final IBlockAccess world, final BlockPos pos) {
        final TileEntityAlarm tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFacing() : EnumFacing.NORTH;
    }
    public void setFacing(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {
        final TileEntityAlarm tileEntity = getTileEntity(world, pos);
        if (tileEntity != null) {
            tileEntity.setFacing(facing);
        }
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

    @Override
    public Class<TileEntityAlarm> getTileEntityClass() {
        return TileEntityAlarm.class;
	}

    @Nullable
    @Override
    public TileEntityAlarm createTileEntity(World world, IBlockState state) {
        return new TileEntityAlarm();
    }

    //public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    //    return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    //}
}