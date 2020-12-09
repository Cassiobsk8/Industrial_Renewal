package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

public class BlockElectricGate extends BlockHorizontalFacing
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool INVERTED = PropertyBool.create("inverted");

    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool LEFT = PropertyBool.create("left");
    public static final PropertyBool RIGHT = PropertyBool.create("right");

    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);

    protected static final AxisAlignedBB CNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB CWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(-0.875D, 0.0D, 0.375D, 0.125D, 1.5D, 0.625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.875, 0.0D, 0.375D, 1.875D, 1.5D, 0.625D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.875D, 0.625D, 1.5D, 1.875D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.875, 0.625D, 1.5D, 0.125D);

    protected static final AxisAlignedBB INORTH_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.375D, 1.875D, 1.5D, 0.625D);
    protected static final AxisAlignedBB ISOUTH_AABB = new AxisAlignedBB(-0.875D, 0.0D, 0.375D, 0.125D, 1.5D, 0.625D);
    protected static final AxisAlignedBB IWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.875, 0.625D, 1.5D, 0.125D);
    protected static final AxisAlignedBB IEAST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.875D, 0.625D, 1.5D, 1.875D);

    public BlockElectricGate(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            IBlockState actualState = state.getActualState(world, pos);
            boolean active = !actualState.getValue(ACTIVE);

            OpenUpAndDown(world, actualState, pos, active);

            EnumFacing facing = actualState.getValue(FACING);
            BlockPos rightPos = pos.offset(facing.rotateY());
            BlockPos leftPos = pos.offset(facing.rotateYCCW());
            IBlockState rightState = world.getBlockState(rightPos);
            IBlockState leftState = world.getBlockState(leftPos);
            boolean inverted = actualState.getValue(INVERTED);

            if (!inverted && rightState.getBlock() instanceof BlockElectricGate && rightState.getActualState(world, rightPos).getValue(INVERTED))
            {
                ((BlockElectricGate) rightState.getBlock()).OpenUpAndDown(world, rightState, rightPos, active);
            }
            else if (inverted && leftState.getBlock() instanceof BlockElectricGate && !leftState.getActualState(world, leftPos).getValue(INVERTED))
            {
                ((BlockElectricGate) leftState.getBlock()).OpenUpAndDown(world, leftState, leftPos, active);
            }

            //Sound
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (active)
            {
                world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, pitch);
            }
            else
            {
                world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, pitch);
            }
        }
        return true;
    }

    public void OpenUpAndDown(World world, IBlockState state, BlockPos pos, boolean active)
    {

        IBlockState upstate = world.getBlockState(pos.up());
        IBlockState dnstate = world.getBlockState(pos.down());
        Block upb = upstate.getBlock();
        Block dnb = dnstate.getBlock();

        state = state.withProperty(ACTIVE, active);
        world.setBlockState(pos, state, 3);
        if (upb instanceof BlockElectricGate)
        {
            OpenUp(world, pos, active);
        }
        if (dnb instanceof BlockElectricGate)
        {
            OpenDown(world, pos, active);
        }
    }

    public void OpenUp(World world, BlockPos pos, boolean active)
    {
        int n = 1;
        while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockElectricGate)
        {
            IBlockState thisState = world.getBlockState(pos.up(n)).withProperty(ACTIVE, active);
            world.setBlockState(pos.up(n), thisState, 3);
            n++;
        }
    }

    public void OpenDown(World world, BlockPos pos, boolean active)
    {
        int n = 1;
        while (world.getBlockState(pos.down(n)).getBlock() instanceof BlockElectricGate)
        {
            IBlockState thisState = world.getBlockState(pos.down(n)).withProperty(ACTIVE, active);
            world.setBlockState(pos.down(n), thisState, 3);
            n++;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        EnumFacing facing = state.getValue(FACING);
        IBlockState rightState = world.getBlockState(pos.offset(facing.rotateY()));
        IBlockState leftState = world.getBlockState(pos.offset(facing.rotateYCCW()));
        Block leftBlock = leftState.getBlock();
        Block rightBlock = rightState.getBlock();
        boolean leftIsGate = (leftBlock instanceof BlockElectricGate);
        boolean rightIsGate = (rightBlock instanceof BlockElectricGate);
        boolean inverted = (leftIsGate && !rightIsGate);
        boolean rightInverted = rightIsGate;
        Block dnb = world.getBlockState(pos.down()).getBlock();
        Block upb = world.getBlockState(pos.up()).getBlock();
        boolean isTop = (dnb instanceof BlockElectricGate) && !(upb instanceof BlockElectricGate);

        state = state.withProperty(UP, isTop).withProperty(INVERTED, inverted)
                .withProperty(LEFT, !inverted)
                .withProperty(RIGHT, !rightInverted);

        return state;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing face = state.getValue(FACING);
        if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH)
        {
            return RNORTH_AABB;
        }
        else
        {
            return RWEST_AABB;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        boolean active = actualState.getValue(ACTIVE);
        EnumFacing face = state.getValue(FACING);
        boolean inverted = actualState.getValue(INVERTED);
        if (active)
        {
            if (face == EnumFacing.NORTH)
            {
                if (inverted)
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, INORTH_AABB);
                }
                else
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                }
            }
            else if (face == EnumFacing.SOUTH)
            {
                if (inverted)
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, ISOUTH_AABB);
                }
                else
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                }
            }
            else if (face == EnumFacing.WEST)
            {
                if (inverted)
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, IWEST_AABB);
                }
                else
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                }
            }
            else if (face == EnumFacing.EAST)
            {
                if (inverted)
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, IEAST_AABB);
                }
                else
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                }
            }
        }
        else
        {
            if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, CNORTH_AABB);
            }
            else if (face == EnumFacing.WEST || face == EnumFacing.EAST)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, CWEST_AABB);
            }
        }

    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, ACTIVE, UP, LEFT, RIGHT, INVERTED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(ACTIVE, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(ACTIVE))
        {
            i |= 4;
        }
        return i;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(ACTIVE, false);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
