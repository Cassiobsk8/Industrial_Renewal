package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.redstone.BlockSignalIndicator;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBaseWall extends BlockBase
{
    public static final PropertyBool CORE = PropertyBool.create("core");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool WEST = PropertyBool.create("west");

    private static float NORTHZ1 = 0.25f;
    private static float SOUTHZ2 = 0.75f;
    private static float WESTX1 = 0.25f;
    private static float EASTX2 = 0.75f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockBaseWall(String name, CreativeTabs tab)
    {
        super(Material.ROCK, name, tab);
        setSoundType(SoundType.STONE);
        setHardness(2f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        IBlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, NORTH))
        {
            NORTHZ1 = 0.0f;
        } else
        {
            NORTHZ1 = 0.25f;
        }
        if (isConnected(actualState, SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else
        {
            SOUTHZ2 = 0.75f;
        }
        if (isConnected(actualState, WEST))
        {
            WESTX1 = 0.0f;
        } else
        {
            WESTX1 = 0.25f;
        }
        if (isConnected(actualState, EAST))
        {
            EASTX2 = 1.0f;
        } else
        {
            EASTX2 = 0.75f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        if (!isActualState)
        {
            state = state.getActualState(worldIn, pos);
        }
        if (isConnected(state, NORTH))
        {
            NORTHZ1 = 0.0f;
        } else
        {
            NORTHZ1 = 0.25f;
        }
        if (isConnected(state, SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else
        {
            SOUTHZ2 = 0.75f;
        }
        if (isConnected(state, WEST))
        {
            WESTX1 = 0.0f;
        } else
        {
            WESTX1 = 0.25f;
        }
        if (isConnected(state, EAST))
        {
            EASTX2 = 1.0f;
        } else
        {
            EASTX2 = 0.75f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, CORE, NORTH, SOUTH, EAST, WEST);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta)
    {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state)
    {
        return 0;
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

    private boolean shouldRenderCenter(IBlockAccess world, BlockPos ownPos)
    {
        return !((canCenterConnectTo(world, ownPos, EnumFacing.NORTH) && canCenterConnectTo(world, ownPos, EnumFacing.SOUTH) && !canCenterConnectTo(world, ownPos, EnumFacing.EAST) && !canCenterConnectTo(world, ownPos, EnumFacing.WEST))
                || (!canCenterConnectTo(world, ownPos, EnumFacing.NORTH) && !canCenterConnectTo(world, ownPos, EnumFacing.SOUTH) && canCenterConnectTo(world, ownPos, EnumFacing.EAST) && canCenterConnectTo(world, ownPos, EnumFacing.WEST)));
    }

    private boolean canCenterConnectTo(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockBaseWall || nb.isFullCube(neighbourState) || nb instanceof BlockWindow;
    }

    private boolean canConnectTo(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockBaseWall || nb.isFullCube(neighbourState)
                || nb instanceof BlockElectricGate
                || nb instanceof BlockLight
                || nb instanceof BlockSignalIndicator
                || nb instanceof BlockWindow;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        state = state.withProperty(NORTH, canConnectTo(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, canConnectTo(world, pos, EnumFacing.SOUTH))
                .withProperty(EAST, canConnectTo(world, pos, EnumFacing.EAST))
                .withProperty(WEST, canConnectTo(world, pos, EnumFacing.WEST))
                .withProperty(CORE, shouldRenderCenter(world, pos));
        return state;
    }

    public final boolean isConnected(final IBlockState state, final PropertyBool property)
    {
        return state.getValue(property);
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
