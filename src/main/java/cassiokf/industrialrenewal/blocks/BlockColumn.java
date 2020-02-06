package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.*;
import cassiokf.industrialrenewal.blocks.redstone.BlockAlarm;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
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

public class BlockColumn extends BlockBase {

    //public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));

    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyInteger PIPE = PropertyInteger.create("pipe", 0, 2);

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
        //return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
        return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, EAST, WEST, PIPE);
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

    protected boolean isValidConnection(final IBlockState neighbourState, final IBlockState ownState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
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
                    || (nb instanceof BlockWireBase && neighbourState.getValue(BlockWireBase.FACING) == neighbourDirection.getOpposite())
                    || nb instanceof BlockPillarEnergyCable || nb instanceof BlockPillarFluidPipe
                    || (nb instanceof BlockAlarm && neighbourState.getValue(BlockAlarm.FACING) == neighbourDirection)
                    || (nb instanceof BlockLight && neighbourState.getValue(BlockLight.FACING) == neighbourDirection.getOpposite());
        }
        if (nb instanceof BlockLight)
        {
            return neighbourState.getValue(BlockLight.FACING) == EnumFacing.UP;
        }
        if (nb instanceof BlockBrace)
        {
            return Arrays.toString(EnumFacing.HORIZONTALS).contains(neighbourState.getValue(BlockBrace.FACING).toString());
        }
        if (nb instanceof BlockFluidPipe)
        {
            return ownState.getValue(PIPE) > 0;
        }
        if (nb instanceof BlockCableTray)
        {
            return neighbourState.getValue(BlockCableTray.BASE).equals(EnumBaseDirection.UP);
        }
        return !(nb instanceof BlockCatwalkLadder)
                && !(nb instanceof BlockSignBase)
                && !(nb instanceof BlockFireExtinguisher)
                && !(nb instanceof BlockAlarm && !(neighbourState.getValue(BlockAlarm.FACING) == neighbourDirection))
                && !nb.isAir(neighbourState, world, ownPos.offset(neighbourDirection));
    }

    private boolean canConnectTo(final IBlockState ownState, final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
        final Block neighbourBlock = neighbourState.getBlock();

        final boolean neighbourIsValidForThis = isValidConnection(neighbourState, ownState, worldIn, ownPos, neighbourDirection);
        final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockColumn) || ((BlockColumn) neighbourBlock).isValidConnection(ownState, neighbourState, worldIn, neighbourPos, neighbourDirection.getOpposite());

        return neighbourIsValidForThis && thisIsValidForNeighbour;
    }

    private int canConnectPipe(IBlockAccess world, BlockPos pos) {
        IBlockState stateOffset = world.getBlockState(pos.down());
        if (stateOffset.getBlock() instanceof BlockPipeBase && !(stateOffset.getBlock() instanceof BlockCableTray))
        {
            if (pipeConnected(world, pos.down(), stateOffset, EnumFacing.DOWN))
            {
                return 0;
            }
            if (!pipeConnected(world, pos.down(), stateOffset, EnumFacing.NORTH) && !pipeConnected(world, pos.down(), stateOffset, EnumFacing.SOUTH))
            {
                return 2;
            }
            if (!pipeConnected(world, pos.down(), stateOffset, EnumFacing.EAST) && !pipeConnected(world, pos.down(), stateOffset, EnumFacing.WEST))
            {
                return 1;
            }
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(PIPE, canConnectPipe(world, pos));
        state = state.withProperty(UP, canConnectTo(state, world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectTo(state, world, pos, EnumFacing.DOWN))
                .withProperty(NORTH, canConnectTo(state, world, pos, EnumFacing.NORTH)).withProperty(SOUTH, canConnectTo(state, world, pos, EnumFacing.SOUTH))
                .withProperty(EAST, canConnectTo(state, world, pos, EnumFacing.EAST)).withProperty(WEST, canConnectTo(state, world, pos, EnumFacing.WEST));

        return state;
    }

    private boolean pipeConnected(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing facing) {
        Block blockOffset = world.getBlockState(pos.offset(facing)).getBlock();
        return blockOffset.equals(state.getBlock());
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
        if (facing == EnumFacing.UP) {
            return state.getValue(UP);
        }
        if (facing == EnumFacing.DOWN) {
            return state.getValue(DOWN);
        }
        if (facing == EnumFacing.NORTH) {
            return state.getValue(NORTH);
        }
        if (facing == EnumFacing.SOUTH) {
            return state.getValue(SOUTH);
        }
        if (facing == EnumFacing.EAST) {
            return state.getValue(EAST);
        }
        return state.getValue(WEST);
        //return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
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
