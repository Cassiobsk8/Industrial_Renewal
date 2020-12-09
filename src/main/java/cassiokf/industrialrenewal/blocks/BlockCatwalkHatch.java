package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
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

public class BlockCatwalkHatch extends BlockHorizontalFacing
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB RSOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
    protected static final AxisAlignedBB REAST_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected static final AxisAlignedBB RDOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

    protected static final AxisAlignedBB OPEN_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB OPEN_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB OPEN_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB OPEN_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockCatwalkHatch(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (state.getValue(ACTIVE))
            {
                world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1.0F);

            }
            else
            {
                world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1.0F);
            }

            state = state.cycleProperty(ACTIVE);
            world.setBlockState(pos, state, 3);
        }
        return true;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IBlockState actualState = state.getActualState(world, pos);
        if (actualState.getValue(ACTIVE))
        {
            return 0;
        }
        else
        {
            return 250;
        }
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
        return world.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing face = state.getValue(FACING);
        Boolean active = state.getValue(ACTIVE);
        if (active)
        {
            if (face == EnumFacing.NORTH)
            {
                return RNORTH_AABB;
            }
            if (face == EnumFacing.SOUTH)
            {
                return RSOUTH_AABB;
            }
            if (face == EnumFacing.WEST)
            {
                return RWEST_AABB;
            }
            return REAST_AABB;
        }
        else
        {
            return RDOWN_AABB;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        Boolean active = actualState.getValue(ACTIVE);
        if (active)
        {
            EnumFacing face = state.getValue(FACING);
            if (face == EnumFacing.NORTH)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_NORTH_AABB);
            }
            else if (face == EnumFacing.SOUTH)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_SOUTH_AABB);
            }
            else if (face == EnumFacing.WEST)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_WEST_AABB);
            }
            else if (face == EnumFacing.EAST)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_EAST_AABB);
            }
        }
        else
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, RDOWN_AABB);
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, ACTIVE);
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
