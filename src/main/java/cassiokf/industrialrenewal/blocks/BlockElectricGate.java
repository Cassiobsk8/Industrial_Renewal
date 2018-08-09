package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IRSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockElectricGate extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public static final PropertyBool UP = PropertyBool.create("up");

    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);

    protected static final AxisAlignedBB CNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB CWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(-0.875D, 0.0D, 0.375D, 0.125D, 1.5D, 0.625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.875, 0.0D, 0.375D, 1.875D, 1.5D, 0.625D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.875D, 0.625D, 1.5D, 1.875D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.875, 0.625D, 1.5D, 0.125D);

    public BlockElectricGate(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState actualState = state.getActualState(world, pos);
        IBlockState upstate = world.getBlockState(pos.up());
        IBlockState dnstate = world.getBlockState(pos.down());
        Block upb = upstate.getBlock();
        Block dnb = dnstate.getBlock();
        if (world.isRemote) {
            return true;
        } else {
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (state.getValue(ACTIVE)) {
                world.playSound(null, pos, IRSoundHandler.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F, pitch);
                if (upb instanceof BlockElectricGate) {
                    upstate = upstate.withProperty(ACTIVE, false);
                    world.setBlockState(pos.up(), upstate, 3);
                }
                if (dnb instanceof BlockElectricGate) {
                    dnstate = dnstate.withProperty(ACTIVE, false);
                    world.setBlockState(pos.down(), dnstate, 3);
                }
                state = actualState.withProperty(ACTIVE, false);
                world.setBlockState(pos, state, 3);
                return true;
            } else {
                world.playSound(null, pos, IRSoundHandler.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F, pitch);
                if (upb instanceof BlockElectricGate) {
                    upstate = upstate.withProperty(ACTIVE, true);
                    world.setBlockState(pos.up(), upstate, 3);
                }
                if (dnb instanceof BlockElectricGate) {
                    dnstate = dnstate.withProperty(ACTIVE, true);
                    world.setBlockState(pos.down(), dnstate, 3);
                }
                state = actualState.withProperty(ACTIVE, true);
                world.setBlockState(pos, state, 3);
                return true;
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        Block dnb = world.getBlockState(pos.down()).getBlock();
        state = state.withProperty(UP, dnb instanceof BlockElectricGate);
        return state;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            return RNORTH_AABB;
        } else {
            return RWEST_AABB;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = getActualState(state, worldIn, pos);
        Boolean active = actualState.getValue(ACTIVE);
        EnumFacing face = state.getValue(FACING);
        if (active) {
            if (face == EnumFacing.NORTH) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            } else if (face == EnumFacing.SOUTH) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            } else if (face == EnumFacing.WEST) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            } else if (face == EnumFacing.EAST) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
        } else {
            if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, CNORTH_AABB);
            } else if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, CWEST_AABB);
            }
        }

    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, UP);
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

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(ACTIVE, false);
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
