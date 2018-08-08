package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.alarm.BlockAlarm;
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
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockColumn extends BlockBase {

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));
    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.3125f;
    private static float UPY2 = 1.0f;


    public BlockColumn(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * Is the neighbouring block a valid connection for this pipe?
     *
     * @param ownState           This pipe's state
     * @param neighbourState     The neighbouring block's state
     * @param world              The world
     * @param ownPos             This pipe's position
     * @param neighbourDirection The direction of the neighbouring block
     * @return Is the neighbouring block a valid connection?
     */
    protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        Block nb = neighbourState.getBlock();

        if ((nb.isFullCube(neighbourState) || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable)
                && neighbourDirection != EnumFacing.UP && neighbourDirection != EnumFacing.DOWN) {

            Block oppositBlock = world.getBlockState(ownPos.offset(neighbourDirection.getOpposite())).getBlock();
            return oppositBlock instanceof BlockColumn || oppositBlock instanceof BlockPillar;
        }
        if (neighbourDirection != EnumFacing.UP && neighbourDirection != EnumFacing.DOWN) {
            if (nb instanceof BlockBrace) {
                return Objects.equals(neighbourState.getValue(BlockBrace.FACING).getName(), neighbourDirection.getOpposite().getName()) || Objects.equals(neighbourState.getValue(BlockBrace.FACING).getName(), "down_" + neighbourDirection.getName());
            }
            return nb instanceof BlockColumn || nb instanceof BlockPillar
                    || (nb instanceof BlockAlarm && neighbourState.getValue(BlockAlarm.FACING) == neighbourDirection)
                    || (nb instanceof BlockLight && neighbourState.getValue(BlockLight.FACING) == neighbourDirection.getOpposite());
        }
        if (nb instanceof BlockLight) {
            return neighbourState.getValue(BlockLight.FACING) == EnumFacing.UP;
        }
        if (nb instanceof BlockBrace) {
            return Arrays.toString(EnumFacing.HORIZONTALS).contains(neighbourState.getValue(BlockBrace.FACING).toString());
        }
        return neighbourDirection == EnumFacing.UP
                || !(nb instanceof BlockCatwalkLadder)
                && !(nb instanceof BlockAlarm && !(neighbourState.getValue(BlockAlarm.FACING) == neighbourDirection))
                && !nb.isAir(neighbourState, world, ownPos.offset(neighbourDirection));
    }

    /**
     * Can this pipe connect to the neighbouring block?
     *
     * @param ownState           This pipe's state
     * @param worldIn            The world
     * @param ownPos             This pipe's position
     * @param neighbourDirection The direction of the neighbouring block
     * @return Can this pipe connect?
     */
    private boolean canConnectTo(final IBlockState ownState, final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
        final Block neighbourBlock = neighbourState.getBlock();

        final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
        final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockColumn) || ((BlockColumn) neighbourBlock).isValidConnection(neighbourState, ownState, worldIn, neighbourPos, neighbourDirection.getOpposite());

        return neighbourIsValidForThis && thisIsValidForNeighbour;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()),
                    canConnectTo(state, world, pos, facing));
        }
        return state;
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
        return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        if (isConnected(state, EnumFacing.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(state, EnumFacing.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(state, EnumFacing.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(state, EnumFacing.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.EAST)) {
            EASTX2 = 0.750f;
        }
        if (isConnected(state, EnumFacing.DOWN)) {
            DOWNY1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.DOWN)) {
            DOWNY1 = 0.3125f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, EnumFacing.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(actualState, EnumFacing.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(actualState, EnumFacing.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(actualState, EnumFacing.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.EAST)) {
            EASTX2 = 0.750f;
        }
        if (isConnected(actualState, EnumFacing.DOWN)) {
            DOWNY1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.DOWN)) {
            DOWNY1 = 0.3125f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
