package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCatWalkStair extends BlockBase {

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE_LEFT = PropertyBool.create("active_left");
    public static final PropertyBool ACTIVE_RIGHT = PropertyBool.create("active_right");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockCatWalkStair(String name) {
        super(Material.IRON, name);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    public void changestate(World world, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        if (world.getBlockState(pos.offset(facing.rotateY())).getBlock() instanceof BlockCatWalkStair) {
            world.setBlockState(pos, state.withProperty(ACTIVE_RIGHT, false));
        }
        if (!(world.getBlockState(pos.offset(facing.rotateY())).getBlock() instanceof BlockCatWalkStair)) {
            world.setBlockState(pos, state.withProperty(ACTIVE_RIGHT, true));
        }
        if (world.getBlockState(pos.offset(facing.rotateY().getOpposite())).getBlock() instanceof BlockCatWalkStair) {
            world.setBlockState(pos, state.withProperty(ACTIVE_LEFT, false));
        }
        if (!(world.getBlockState(pos.offset(facing.rotateY().getOpposite())).getBlock() instanceof BlockCatWalkStair)) {
            world.setBlockState(pos, state.withProperty(ACTIVE_LEFT, true));
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        changestate(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        changestate(world, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        changestate(worldIn, pos, state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE_LEFT, ACTIVE_RIGHT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);

        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingbits = state.getValue(FACING).getIndex();
        return facingbits;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);

        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        EnumFacing face = state.getValue(FACING);
        if (face == EnumFacing.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        }
        if (face == EnumFacing.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        }
        if (face == EnumFacing.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
        }
        if (face == EnumFacing.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
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
}
