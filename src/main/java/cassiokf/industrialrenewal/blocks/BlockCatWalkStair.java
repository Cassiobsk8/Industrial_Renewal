package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE_LEFT = PropertyBool.create("active_left");
    public static final PropertyBool ACTIVE_RIGHT = PropertyBool.create("active_right");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 0.03125D);
    protected static final AxisAlignedBB SC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 2.0D, 1.0D);
    protected static final AxisAlignedBB WC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 2.0D, 1.0D);
    protected static final AxisAlignedBB EC_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);

    public BlockCatWalkStair(String name) {
        super(Material.IRON, name);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    public void changestate(World world, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        Block blockRight = world.getBlockState(pos.offset(facing.rotateY())).getBlock();
        Block blockLeft = world.getBlockState(pos.offset(facing.rotateY().getOpposite())).getBlock();
        state = world.getBlockState(pos);
        if (blockRight instanceof BlockCatWalkStair && blockLeft instanceof BlockCatWalkStair) {
            world.setBlockState(pos, state.withProperty(ACTIVE_LEFT, false).withProperty(ACTIVE_RIGHT, false));
        }
        if (blockRight instanceof BlockCatWalkStair && !(blockLeft instanceof BlockCatWalkStair)) {
            world.setBlockState(pos, state.withProperty(ACTIVE_LEFT, true).withProperty(ACTIVE_RIGHT, false));
        }
        if (!(blockRight instanceof BlockCatWalkStair) && blockLeft instanceof BlockCatWalkStair) {
            world.setBlockState(pos, state.withProperty(ACTIVE_LEFT, false).withProperty(ACTIVE_RIGHT, true));
        }
        if (!(blockRight instanceof BlockCatWalkStair) && !(blockLeft instanceof BlockCatWalkStair)) {
            world.setBlockState(pos, state.withProperty(ACTIVE_LEFT, true).withProperty(ACTIVE_RIGHT, true));
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        changestate(world, pos, state);
        world.notifyNeighborsOfStateChange(pos, this, false);
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        changestate(world, pos, state);
        world.notifyNeighborsOfStateChange(pos, this, false);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        world.notifyNeighborsOfStateChange(pos, this, false);
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
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(ACTIVE_LEFT, Boolean.valueOf((meta & 4) > 0)).withProperty(ACTIVE_RIGHT, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(ACTIVE_LEFT)) {
            i |= 4;
        }
        if (state.getValue(ACTIVE_RIGHT)) {
            i |= 8;
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = getActualState(state, worldIn, pos);

        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);

        EnumFacing face = actualState.getValue(FACING);
        Boolean left = actualState.getValue(ACTIVE_LEFT);
        Boolean right = actualState.getValue(ACTIVE_RIGHT);
        if (face == EnumFacing.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }

        }
        if (face == EnumFacing.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
        }
        if (face == EnumFacing.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
        }
        if (face == EnumFacing.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
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
