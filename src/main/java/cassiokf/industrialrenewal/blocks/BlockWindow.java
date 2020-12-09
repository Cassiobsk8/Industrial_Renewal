package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockWindow extends BlockBase
{
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));
    private static float NORTHZ1 = 0.4375f;
    private static float SOUTHZ2 = 0.5625f;
    private static float WESTX1 = 0.4375f;
    private static float EASTX2 = 0.5625f;
    private static final float DOWNY1 = 0.0f;
    private static final float UPY2 = 1.0f;

    public BlockWindow(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        IBlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.0f;
        }
        else
        {
            NORTHZ1 = 0.4375f;
        }
        if (isConnected(actualState, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        }
        else
        {
            SOUTHZ2 = 0.5625f;
        }
        if (isConnected(actualState, EnumFacing.WEST))
        {
            WESTX1 = 0.0f;
        }
        else
        {
            WESTX1 = 0.4375f;
        }
        if (isConnected(actualState, EnumFacing.EAST))
        {
            EASTX2 = 1.0f;
        }
        else
        {
            EASTX2 = 0.5625f;
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
        if (isConnected(state, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.0f;
        }
        else
        {
            NORTHZ1 = 0.4375f;
        }
        if (isConnected(state, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        }
        else
        {
            SOUTHZ2 = 0.5625f;
        }
        if (isConnected(state, EnumFacing.WEST))
        {
            WESTX1 = 0.0f;
        }
        else
        {
            WESTX1 = 0.4375f;
        }
        if (isConnected(state, EnumFacing.EAST))
        {
            EASTX2 = 1.0f;
        }
        else
        {
            EASTX2 = 0.5625f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
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

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    private boolean shouldRenderCenter(IBlockAccess world, BlockPos ownPos)
    {
        return (!isThisConnected(world, ownPos, EnumFacing.SOUTH) && !isThisConnected(world, ownPos, EnumFacing.NORTH) && !isThisConnected(world, ownPos, EnumFacing.WEST) && !isThisConnected(world, ownPos, EnumFacing.EAST))
                || ((isThisConnected(world, ownPos, EnumFacing.SOUTH) && isThisConnected(world, ownPos, EnumFacing.WEST))
                || (isThisConnected(world, ownPos, EnumFacing.SOUTH) && isThisConnected(world, ownPos, EnumFacing.EAST))
                || (isThisConnected(world, ownPos, EnumFacing.NORTH) && isThisConnected(world, ownPos, EnumFacing.WEST))
                || (isThisConnected(world, ownPos, EnumFacing.NORTH) && isThisConnected(world, ownPos, EnumFacing.EAST)))
                || (sidesConnected(world, ownPos) == 1);
    }

    private int sidesConnected(IBlockAccess world, BlockPos pos)
    {
        int sides = 0;
        for (EnumFacing faces : EnumFacing.HORIZONTALS)
        {
            IBlockState neighbourState = world.getBlockState(pos.offset(faces));
            Block nb = neighbourState.getBlock();
            if (nb instanceof BlockWindow || nb.isFullCube(neighbourState) || nb instanceof BlockBaseWall)
            {
                sides++;
            }
        }
        return sides;
    }

    protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        if (neighbourDirection == EnumFacing.DOWN)
        {
            return false;
        }
        if (neighbourDirection == EnumFacing.UP)
        {
            return shouldRenderCenter(world, ownPos);
        }
        return nb instanceof BlockWindow || nb.isFullCube(neighbourState) || nb instanceof BlockBaseWall;
    }

    private boolean isThisConnected(IBlockAccess world, BlockPos pos, EnumFacing neighbourFacing)
    {
        IBlockState neighbourState = world.getBlockState(pos.offset(neighbourFacing));
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockWindow || nb.isFullCube(neighbourState) || nb instanceof BlockBaseWall;
    }

    private boolean canConnectTo(final IBlockState ownState, final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
        return isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        for (final EnumFacing facing : EnumFacing.VALUES)
        {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()),
                    canConnectTo(state, world, pos, facing));
        }
        return state;
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing)
    {
        return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH)
        {
            return BlockFaceShape.SOLID;
        }
        else
        {
            return BlockFaceShape.UNDEFINED;
        }
    }
}
