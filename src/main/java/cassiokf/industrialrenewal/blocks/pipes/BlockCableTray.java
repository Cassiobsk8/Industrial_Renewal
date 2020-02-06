package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.properties.PropertyBaseDirection;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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

public class BlockCableTray extends BlockPipeBase<TileEntityCableTray> implements ITileEntityProvider
{
    public static final IProperty<EnumBaseDirection> BASE = PropertyBaseDirection.create("base");

    public static final IUnlistedProperty<Boolean> PIPE_CORE = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_core"));
    public static final IUnlistedProperty<Boolean> PIPE_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_north"));
    public static final IUnlistedProperty<Boolean> PIPE_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_south"));
    public static final IUnlistedProperty<Boolean> PIPE_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_east"));
    public static final IUnlistedProperty<Boolean> PIPE_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_west"));
    public static final IUnlistedProperty<Boolean> PIPE_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_up"));
    public static final IUnlistedProperty<Boolean> PIPE_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_down"));

    public static final IUnlistedProperty<Boolean> PIPE2_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_north"));
    public static final IUnlistedProperty<Boolean> PIPE2_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_south"));
    public static final IUnlistedProperty<Boolean> PIPE2_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_east"));
    public static final IUnlistedProperty<Boolean> PIPE2_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_west"));
    public static final IUnlistedProperty<Boolean> PIPE2_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_up"));
    public static final IUnlistedProperty<Boolean> PIPE2_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_down"));

    public static final IUnlistedProperty<Boolean> HV_CORE = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_core"));
    public static final IUnlistedProperty<Boolean> HV_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_north"));
    public static final IUnlistedProperty<Boolean> HV_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_south"));
    public static final IUnlistedProperty<Boolean> HV_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_east"));
    public static final IUnlistedProperty<Boolean> HV_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_west"));
    public static final IUnlistedProperty<Boolean> HV_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_up"));
    public static final IUnlistedProperty<Boolean> HV_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_down"));

    public static final IUnlistedProperty<Boolean> HV2_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_north"));
    public static final IUnlistedProperty<Boolean> HV2_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_south"));
    public static final IUnlistedProperty<Boolean> HV2_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_east"));
    public static final IUnlistedProperty<Boolean> HV2_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_west"));
    public static final IUnlistedProperty<Boolean> HV2_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_up"));
    public static final IUnlistedProperty<Boolean> HV2_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_down"));

    public static final IUnlistedProperty<Boolean> MV_CORE = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_core"));
    public static final IUnlistedProperty<Boolean> MV_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_north"));
    public static final IUnlistedProperty<Boolean> MV_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_south"));
    public static final IUnlistedProperty<Boolean> MV_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_east"));
    public static final IUnlistedProperty<Boolean> MV_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_west"));
    public static final IUnlistedProperty<Boolean> MV_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_up"));
    public static final IUnlistedProperty<Boolean> MV_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_down"));

    public static final IUnlistedProperty<Boolean> MV2_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_north"));
    public static final IUnlistedProperty<Boolean> MV2_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_south"));
    public static final IUnlistedProperty<Boolean> MV2_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_east"));
    public static final IUnlistedProperty<Boolean> MV2_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_west"));
    public static final IUnlistedProperty<Boolean> MV2_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_up"));
    public static final IUnlistedProperty<Boolean> MV2_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_down"));

    public static final IUnlistedProperty<Boolean> LV_CORE = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_core"));
    public static final IUnlistedProperty<Boolean> LV_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_north"));
    public static final IUnlistedProperty<Boolean> LV_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_south"));
    public static final IUnlistedProperty<Boolean> LV_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_east"));
    public static final IUnlistedProperty<Boolean> LV_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_west"));
    public static final IUnlistedProperty<Boolean> LV_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_up"));
    public static final IUnlistedProperty<Boolean> LV_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe_down"));

    public static final IUnlistedProperty<Boolean> LV2_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_north"));
    public static final IUnlistedProperty<Boolean> LV2_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_south"));
    public static final IUnlistedProperty<Boolean> LV2_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_east"));
    public static final IUnlistedProperty<Boolean> LV2_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_west"));
    public static final IUnlistedProperty<Boolean> LV2_UP = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_up"));
    public static final IUnlistedProperty<Boolean> LV2_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("pipe2_down"));

    public static final IUnlistedProperty<Boolean> DATA_CORE = new Properties.PropertyAdapter<>(PropertyBool.create("data_core"));

    public BlockCableTray(String name, CreativeTabs tab)
    {
        super(name, tab);
        this.setDefaultState(this.getDefaultState().withProperty(BASE, EnumBaseDirection.NONE));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("Can place energy Cable and Fluid Pipe in one block");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (hand.equals(EnumHand.MAIN_HAND))
        {
            Item item = playerIn.getHeldItemMainhand().getItem();
            Block block = Block.getBlockFromItem(item);
            if (item instanceof ItemPowerScrewDrive || block instanceof BlockFluidPipe || block instanceof BlockEnergyCable)
            {
                TileEntityCableTray te = (TileEntityCableTray) worldIn.getTileEntity(pos);
                if (te != null)
                {
                    boolean change = te.onBlockActivated(playerIn, playerIn.getHeldItemMainhand());
                    if (change) worldIn.notifyBlockUpdate(pos, state, state, 3);
                    return change;
                }
            }
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{BASE}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, PIPE_CORE,
                PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST, PIPE_UP, PIPE_DOWN, PIPE2_NORTH, PIPE2_SOUTH, PIPE2_EAST,
                PIPE2_WEST, PIPE2_UP, PIPE2_DOWN,
                HV_CORE, HV_NORTH, HV_SOUTH, HV_EAST, HV_WEST, HV_UP, HV_DOWN, HV2_NORTH, HV2_SOUTH,
                HV2_EAST, HV2_WEST, HV2_UP, HV2_DOWN,
                MV_CORE, MV_NORTH, MV_SOUTH, MV_EAST, MV_WEST, MV_UP, MV_DOWN, MV2_NORTH, MV2_SOUTH,
                MV2_EAST, MV2_WEST, MV2_UP, MV2_DOWN,
                LV_CORE, LV_NORTH, LV_SOUTH, LV_EAST, LV_WEST, LV_UP, LV_DOWN, LV2_NORTH, LV2_SOUTH,
                LV2_EAST, LV2_WEST, LV2_UP, LV2_DOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        EnumBaseDirection direction = EnumBaseDirection.NONE;
        Block block = world.getBlockState(pos.offset(facing.getOpposite())).getBlock();
        if (!(block instanceof BlockCableTray))
        {
            direction = EnumBaseDirection.byIndex(facing.getOpposite().getIndex());
        }

        return this.getDefaultState().withProperty(BASE, direction);
    }

    @Override
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        IBlockState state = worldIn.getBlockState(ownPos.offset(neighbourDirection));
        return state.getBlock() instanceof BlockCableTray;
    }

    @Override
    public boolean canConnectToCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        return state.getBlock() instanceof BlockFluidPipe;
    }

    private boolean canConnectToEnergyCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection, EnumCableIn type)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof BlockEnergyCable
                && type.equals(BlockEnergyCable.convertFromType(((BlockEnergyCable) block).type));
    }


    private boolean canConnectFluidPipeTrayToTray(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        TileEntity otherTE = worldIn.getTileEntity(ownPos.offset(neighbourDirection));
        if (otherTE instanceof TileEntityCableTray)
        {
            return ((TileEntityCableTray) otherTE).hasPipe();
        }
        return false;
    }

    private boolean isPipePresent(IBlockAccess world, BlockPos pos)
    {
        TileEntityCableTray te = (TileEntityCableTray) world.getTileEntity(pos);
        if (te != null)
        {
            return te.hasPipe();
        }
        return false;
    }

    public boolean isCablePresent(IBlockAccess world, BlockPos pos, EnumCableIn type)
    {
        TileEntityCableTray te = (TileEntityCableTray) world.getTileEntity(pos);
        if (te != null)
        {
            return te.getCableIn().equals(type);
        }
        return false;
    }

    private boolean canConnectCableTrayToTray(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        TileEntity thisTE = worldIn.getTileEntity(ownPos);
        TileEntity otherTE = worldIn.getTileEntity(ownPos.offset(neighbourDirection));
        if (thisTE instanceof TileEntityCableTray && otherTE instanceof TileEntityCableTray)
        {
            return ((TileEntityCableTray) thisTE).getCableIn().equals(((TileEntityCableTray) otherTE).getCableIn());
        }
        return false;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) state;
            boolean isHvPresent = isCablePresent(world, pos, EnumCableIn.HV);
            boolean isMvPresent = isCablePresent(world, pos, EnumCableIn.MV);
            boolean isLvPresent = isCablePresent(world, pos, EnumCableIn.LV);
            boolean isPipePresent = isPipePresent(world, pos);
            return eState.withProperty(MASTER, isMaster(world, pos))
                    .withProperty(SOUTH, canConnectToPipe(world, pos, EnumFacing.SOUTH)).withProperty(NORTH, canConnectToPipe(world, pos, EnumFacing.NORTH))
                    .withProperty(EAST, canConnectToPipe(world, pos, EnumFacing.EAST)).withProperty(WEST, canConnectToPipe(world, pos, EnumFacing.WEST))
                    .withProperty(UP, canConnectToPipe(world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectToPipe(world, pos, EnumFacing.DOWN))
                    .withProperty(PIPE_CORE, isPipePresent)
                    .withProperty(PIPE_NORTH, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, EnumFacing.NORTH))
                    .withProperty(PIPE_SOUTH, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, EnumFacing.SOUTH))
                    .withProperty(PIPE_EAST, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, EnumFacing.EAST))
                    .withProperty(PIPE_WEST, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, EnumFacing.WEST))
                    .withProperty(PIPE_UP, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, EnumFacing.UP))
                    .withProperty(PIPE_DOWN, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, EnumFacing.DOWN))
                    .withProperty(PIPE2_NORTH, isPipePresent && canConnectToCapability(world, pos, EnumFacing.NORTH))
                    .withProperty(PIPE2_SOUTH, isPipePresent && canConnectToCapability(world, pos, EnumFacing.SOUTH))
                    .withProperty(PIPE2_EAST, isPipePresent && canConnectToCapability(world, pos, EnumFacing.EAST))
                    .withProperty(PIPE2_WEST, isPipePresent && canConnectToCapability(world, pos, EnumFacing.WEST))
                    .withProperty(PIPE2_UP, isPipePresent && canConnectToCapability(world, pos, EnumFacing.UP))
                    .withProperty(PIPE2_DOWN, isPipePresent && canConnectToCapability(world, pos, EnumFacing.DOWN))
                    .withProperty(HV_CORE, isHvPresent)
                    .withProperty(HV_NORTH, isHvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.NORTH))
                    .withProperty(HV_SOUTH, isHvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.SOUTH))
                    .withProperty(HV_EAST, isHvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.EAST))
                    .withProperty(HV_WEST, isHvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.WEST))
                    .withProperty(HV_UP, isHvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.UP))
                    .withProperty(HV_DOWN, isHvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.DOWN))
                    .withProperty(HV2_NORTH, isHvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.NORTH, EnumCableIn.HV))
                    .withProperty(HV2_SOUTH, isHvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.SOUTH, EnumCableIn.HV))
                    .withProperty(HV2_EAST, isHvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.EAST, EnumCableIn.HV))
                    .withProperty(HV2_WEST, isHvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.WEST, EnumCableIn.HV))
                    .withProperty(HV2_UP, isHvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.UP, EnumCableIn.HV))
                    .withProperty(HV2_DOWN, isHvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.DOWN, EnumCableIn.HV))
                    .withProperty(MV_CORE, isMvPresent)
                    .withProperty(MV_NORTH, isMvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.NORTH))
                    .withProperty(MV_SOUTH, isMvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.SOUTH))
                    .withProperty(MV_EAST, isMvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.EAST))
                    .withProperty(MV_WEST, isMvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.WEST))
                    .withProperty(MV_UP, isMvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.UP))
                    .withProperty(MV_DOWN, isMvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.DOWN))
                    .withProperty(MV2_NORTH, isMvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.NORTH, EnumCableIn.MV))
                    .withProperty(MV2_SOUTH, isMvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.SOUTH, EnumCableIn.MV))
                    .withProperty(MV2_EAST, isMvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.EAST, EnumCableIn.MV))
                    .withProperty(MV2_WEST, isMvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.WEST, EnumCableIn.MV))
                    .withProperty(MV2_UP, isMvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.UP, EnumCableIn.MV))
                    .withProperty(MV2_DOWN, isMvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.DOWN, EnumCableIn.MV))
                    .withProperty(LV_CORE, isLvPresent)
                    .withProperty(LV_NORTH, isLvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.NORTH))
                    .withProperty(LV_SOUTH, isLvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.SOUTH))
                    .withProperty(LV_EAST, isLvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.EAST))
                    .withProperty(LV_WEST, isLvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.WEST))
                    .withProperty(LV_UP, isLvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.UP))
                    .withProperty(LV_DOWN, isLvPresent && canConnectCableTrayToTray(world, pos, EnumFacing.DOWN))
                    .withProperty(LV2_NORTH, isLvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.NORTH, EnumCableIn.LV))
                    .withProperty(LV2_SOUTH, isLvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.SOUTH, EnumCableIn.LV))
                    .withProperty(LV2_EAST, isLvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.EAST, EnumCableIn.LV))
                    .withProperty(LV2_WEST, isLvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.WEST, EnumCableIn.LV))
                    .withProperty(LV2_UP, isLvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.UP, EnumCableIn.LV))
                    .withProperty(LV2_DOWN, isLvPresent && canConnectToEnergyCapability(world, pos, EnumFacing.DOWN, EnumCableIn.LV));
        }
        return state;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BASE, EnumBaseDirection.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BASE).getIndex();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass()
    {
        return TileEntityCableTray.class;
    }

    @Nullable
    @Override
    public TileEntityCableTray createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCableTray();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCableTray();
    }
}
