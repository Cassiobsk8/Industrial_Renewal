package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockTileEntityConnectedMultiblocks;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPipeBase<TE extends TileEntityMultiBlocksTube> extends BlockTileEntityConnectedMultiblocks<TE>
{

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.250f;
    private static float UPY2 = 0.750f;

    public static final IUnlistedProperty<Boolean> MASTER = new Properties.PropertyAdapter<>(PropertyBool.create("master"));
    public static final IUnlistedProperty<Boolean> CSOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("c_south"));
    public static final IUnlistedProperty<Boolean> CNORTH = new Properties.PropertyAdapter<>(PropertyBool.create("c_north"));
    public static final IUnlistedProperty<Boolean> CEAST = new Properties.PropertyAdapter<>(PropertyBool.create("c_east"));
    public static final IUnlistedProperty<Boolean> CWEST = new Properties.PropertyAdapter<>(PropertyBool.create("c_west"));
    public static final IUnlistedProperty<Boolean> CUP = new Properties.PropertyAdapter<>(PropertyBool.create("c_up"));
    public static final IUnlistedProperty<Boolean> CDOWN = new Properties.PropertyAdapter<>(PropertyBool.create("c_down"));

    //Pillar use Only
    public static final IUnlistedProperty<Boolean> WSOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("w_south"));
    public static final IUnlistedProperty<Boolean> WNORTH = new Properties.PropertyAdapter<>(PropertyBool.create("w_north"));
    public static final IUnlistedProperty<Boolean> WEAST = new Properties.PropertyAdapter<>(PropertyBool.create("w_east"));
    public static final IUnlistedProperty<Boolean> WWEST = new Properties.PropertyAdapter<>(PropertyBool.create("w_west"));
    public static final IUnlistedProperty<Boolean> WUP = new Properties.PropertyAdapter<>(PropertyBool.create("w_up"));
    public static final IUnlistedProperty<Boolean> WDOWN = new Properties.PropertyAdapter<>(PropertyBool.create("w_down"));

    public BlockPipeBase(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[]{}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState();
    }

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

    public abstract boolean canConnectToPipe(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection);

    public abstract boolean canConnectToCapability(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection);

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) state;
            return eState.withProperty(MASTER, IRConfig.MainConfig.Main.showMaster && isMaster(world, pos))
                    .withProperty(SOUTH, canConnectToPipe(world, pos, EnumFacing.SOUTH)).withProperty(NORTH, canConnectToPipe(world, pos, EnumFacing.NORTH))
                    .withProperty(EAST, canConnectToPipe(world, pos, EnumFacing.EAST)).withProperty(WEST, canConnectToPipe(world, pos, EnumFacing.WEST))
                    .withProperty(UP, canConnectToPipe(world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectToPipe(world, pos, EnumFacing.DOWN))
                    .withProperty(CSOUTH, canConnectToCapability(world, pos, EnumFacing.SOUTH)).withProperty(CNORTH, canConnectToCapability(world, pos, EnumFacing.NORTH))
                    .withProperty(CEAST, canConnectToCapability(world, pos, EnumFacing.EAST)).withProperty(CWEST, canConnectToCapability(world, pos, EnumFacing.WEST))
                    .withProperty(CUP, canConnectToCapability(world, pos, EnumFacing.UP)).withProperty(CDOWN, canConnectToCapability(world, pos, EnumFacing.DOWN));
        }
        return state;
    }

    public boolean isMaster(IBlockAccess world, BlockPos pos)
    {
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) world.getTileEntity(pos);
        return te != null && te.isMaster();
    }

    public final boolean isConnected(IBlockAccess world, BlockPos pos, IBlockState state, final IUnlistedProperty<Boolean> property)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) getExtendedState(state, world, pos);
            return eState.getValue(property);
        }
        return false;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
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

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

}
