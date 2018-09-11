package cassiokf.industrialrenewal.tileentity.fusebox;

import cassiokf.industrialrenewal.blocks.BlockFuseBoxConduitExtension;
import cassiokf.industrialrenewal.blocks.BlockTileEntity;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFuseBox extends BlockTileEntity<TileEntityFuseBox> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool DOWNCONDUIT = PropertyBool.create("down");
    public static final PropertyBool UPCONDUIT = PropertyBool.create("up");


    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.25F, 0.3125F, 0.875F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125, 0.25F, 0.6875F, 0.875F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125, 0.6875F, 0.75D, 0.875F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125, 0.3125F, 0.75D, 0.875F, 0);

    public BlockFuseBox(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityFuseBox te = (TileEntityFuseBox) world.getTileEntity(pos);
        if (!player.isSneaking()) {
            te.changeActivate();
            return true;
        } else {
            //Open GUI
        }
        return false;
    }

    private boolean canConnectConduit(int side, IBlockAccess world, BlockPos pos) {
        BlockPos posoff;
        if (side == 0) { //up
            posoff = pos.offset(EnumFacing.UP);
        } else {
            posoff = pos.offset(EnumFacing.DOWN);
        }
        return world.getBlockState(posoff).getBlock() instanceof BlockFuseBoxConduitExtension;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(DOWNCONDUIT, canConnectConduit(1, world, pos)).withProperty(UPCONDUIT, canConnectConduit(0, world, pos));
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(ACTIVE, false).withProperty(DOWNCONDUIT, false).withProperty(UPCONDUIT, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, DOWNCONDUIT, UPCONDUIT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(ACTIVE, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(ACTIVE)) {
            i |= 4;
        }
        return i;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getActualState(source, pos).getValue(FACING)) {
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
    public Class<TileEntityFuseBox> getTileEntityClass() {
        return TileEntityFuseBox.class;
    }

    @Nullable
    @Override
    public TileEntityFuseBox createTileEntity(World world, IBlockState state) {
        return new TileEntityFuseBox();
    }
}
