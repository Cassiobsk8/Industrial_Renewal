package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockTileEntityConnectedMultiblocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public abstract class BlockPipeBase<TE extends TileEntityMultiBlocksTube> extends BlockTileEntityConnectedMultiblocks<TE>
{

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.250f;
    private static float UPY2 = 0.750f;

    //public static final IUnlistedProperty<Boolean> MASTER = new Properties.PropertyAdapter<>(BooleanProperty.create("master"));
    //public static final IUnlistedProperty<Boolean> CSOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("c_south"));
    //public static final IUnlistedProperty<Boolean> CNORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("c_north"));
    //public static final IUnlistedProperty<Boolean> CEAST = new Properties.PropertyAdapter<>(BooleanProperty.create("c_east"));
    //public static final IUnlistedProperty<Boolean> CWEST = new Properties.PropertyAdapter<>(BooleanProperty.create("c_west"));
    //public static final IUnlistedProperty<Boolean> CUP = new Properties.PropertyAdapter<>(BooleanProperty.create("c_up"));
    //public static final IUnlistedProperty<Boolean> CDOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("c_down"));

    //Pillar use Only
    //public static final IUnlistedProperty<Boolean> WSOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("w_south"));
    //public static final IUnlistedProperty<Boolean> WNORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("w_north"));
    //public static final IUnlistedProperty<Boolean> WEAST = new Properties.PropertyAdapter<>(BooleanProperty.create("w_east"));
    //public static final IUnlistedProperty<Boolean> WWEST = new Properties.PropertyAdapter<>(BooleanProperty.create("w_west"));
    //public static final IUnlistedProperty<Boolean> WUP = new Properties.PropertyAdapter<>(BooleanProperty.create("w_up"));
    //public static final IUnlistedProperty<Boolean> WDOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("w_down"));

    public BlockPipeBase(Block.Properties property)
    {
        super(property);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState();
    }


    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    public abstract boolean canConnectToPipe(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighborDirection);

    public abstract boolean canConnectToCapability(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighborDirection);

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    return eState.with(MASTER, IRConfig.MainConfig.Main.showMaster && isMaster(world, pos))
        //            .with(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH)).with(NORTH, canConnectToPipe(world, pos, Direction.NORTH))
        //            .with(EAST, canConnectToPipe(world, pos, Direction.EAST)).with(WEST, canConnectToPipe(world, pos, Direction.WEST))
        //            .with(UP, canConnectToPipe(world, pos, Direction.UP)).with(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
        //            .with(CSOUTH, canConnectToCapability(world, pos, Direction.SOUTH)).with(CNORTH, canConnectToCapability(world, pos, Direction.NORTH))
        //            .with(CEAST, canConnectToCapability(world, pos, Direction.EAST)).with(CWEST, canConnectToCapability(world, pos, Direction.WEST))
        //            .with(CUP, canConnectToCapability(world, pos, Direction.UP)).with(CDOWN, canConnectToCapability(world, pos, Direction.DOWN));
        //}
        return state;
    }

    public boolean isMaster(IBlockReader world, BlockPos pos)
    {
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) world.getTileEntity(pos);
        return te != null && te.isMaster();
    }
/*
    public final boolean isConnected(IBlockReader world, BlockPos pos, BlockState state, final IUnlistedProperty<Boolean> property)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) getExtendedState(state, world, pos);
            return eState.get(property);
        }
        return false;
    }

    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        if (isConnected(worldIn, pos, state, NORTH) || isConnected(worldIn, pos, state, CNORTH))
        {
            NORTHZ1 = 0.0f;
        } else
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, SOUTH) || isConnected(worldIn, pos, state, CSOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, WEST) || isConnected(worldIn, pos, state, CWEST))
        {
            WESTX1 = 0.0f;
        } else
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, EAST) || isConnected(worldIn, pos, state, CEAST))
        {
            EASTX2 = 1.0f;
        } else
        {
            EASTX2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, DOWN) || isConnected(worldIn, pos, state, CDOWN))
        {
            DOWNY1 = 0.0f;
        } else
        {
            DOWNY1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, UP) || isConnected(worldIn, pos, state, CUP))
        {
            UPY2 = 1.0f;
        } else
        {
            UPY2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        if (isConnected(worldIn, pos, state, NORTH) || isConnected(worldIn, pos, state, CNORTH))
        {
            NORTHZ1 = 0.0f;
        } else
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, SOUTH) || isConnected(worldIn, pos, state, CSOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, WEST) || isConnected(worldIn, pos, state, CWEST))
        {
            WESTX1 = 0.0f;
        } else
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, EAST) || isConnected(worldIn, pos, state, CEAST))
        {
            EASTX2 = 1.0f;
        } else
        {
            EASTX2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, DOWN) || isConnected(worldIn, pos, state, CDOWN))
        {
            DOWNY1 = 0.0f;
        } else
        {
            DOWNY1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, UP) || isConnected(worldIn, pos, state, CUP))
        {
            UPY2 = 1.0f;
        } else
        {
            UPY2 = 0.750f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }
*/
}
