package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockLight extends BlockBase
{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.3125D, 0.1875D, 0.0D, 0.6875D, 0.8125D, 0.3125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.1875D, 0.6875D, 0.6875D, 0.8125D, 1.0);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.6875D, 0.1875D, 0.3125D, 1.0, 0.8125D, 0.6875D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.1875D, 0.3125D, 0.3125D, 0.8125D, 0.6875D);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.3125D, 0.6875D, 0.1875D, 0.6875D, 1.0D, 0.8125D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.1875D, 0.6875D, 0.3125D, 0.8125D);

    public BlockLight(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setLightLevel(1.0F);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        EnumFacing dir = actualState.getValue(FACING);
        if (dir == EnumFacing.NORTH)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        }
        if (dir == EnumFacing.SOUTH)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        }
        if (dir == EnumFacing.EAST)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        }
        if (dir == EnumFacing.WEST)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
        }
        if (dir == EnumFacing.DOWN)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
        }
        if (dir == EnumFacing.UP)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, UP_AABB);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        IBlockState actualState = getActualState(state, source, pos);
        EnumFacing dir = actualState.getValue(FACING);
        switch (dir)
        {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case DOWN:
                return DOWN_AABB;
            default:
                return UP_AABB;
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @Deprecated
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
