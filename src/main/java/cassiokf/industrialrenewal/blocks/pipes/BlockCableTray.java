package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.properties.PropertyBaseDirection;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCableTray extends BlockPipeBase<TileEntityCableTray>
{
    public static final IProperty<EnumBaseDirection> BASE = PropertyBaseDirection.create("base");

    //public static final IUnlistedProperty<Boolean> PIPE_CORE = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_core"));
    //public static final IUnlistedProperty<Boolean> PIPE_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_north"));
    //public static final IUnlistedProperty<Boolean> PIPE_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_south"));
    //public static final IUnlistedProperty<Boolean> PIPE_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_east"));
    //public static final IUnlistedProperty<Boolean> PIPE_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_west"));
    //public static final IUnlistedProperty<Boolean> PIPE_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_up"));
    //public static final IUnlistedProperty<Boolean> PIPE_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_down"));

    //public static final IUnlistedProperty<Boolean> PIPE2_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_north"));
    //public static final IUnlistedProperty<Boolean> PIPE2_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_south"));
    //public static final IUnlistedProperty<Boolean> PIPE2_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_east"));
    //public static final IUnlistedProperty<Boolean> PIPE2_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_west"));
    //public static final IUnlistedProperty<Boolean> PIPE2_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_up"));
    //public static final IUnlistedProperty<Boolean> PIPE2_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_down"));

    //public static final IUnlistedProperty<Boolean> HV_CORE = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_core"));
    //public static final IUnlistedProperty<Boolean> HV_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_north"));
    //public static final IUnlistedProperty<Boolean> HV_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_south"));
    //public static final IUnlistedProperty<Boolean> HV_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_east"));
    //public static final IUnlistedProperty<Boolean> HV_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_west"));
    //public static final IUnlistedProperty<Boolean> HV_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_up"));
    //public static final IUnlistedProperty<Boolean> HV_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_down"));

    //public static final IUnlistedProperty<Boolean> HV2_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_north"));
    //public static final IUnlistedProperty<Boolean> HV2_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_south"));
    //public static final IUnlistedProperty<Boolean> HV2_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_east"));
    //public static final IUnlistedProperty<Boolean> HV2_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_west"));
    //public static final IUnlistedProperty<Boolean> HV2_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_up"));
    //public static final IUnlistedProperty<Boolean> HV2_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_down"));

    //public static final IUnlistedProperty<Boolean> MV_CORE = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_core"));
    //public static final IUnlistedProperty<Boolean> MV_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_north"));
    //public static final IUnlistedProperty<Boolean> MV_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_south"));
    //public static final IUnlistedProperty<Boolean> MV_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_east"));
    //public static final IUnlistedProperty<Boolean> MV_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_west"));
    //public static final IUnlistedProperty<Boolean> MV_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_up"));
    //public static final IUnlistedProperty<Boolean> MV_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_down"));

    //public static final IUnlistedProperty<Boolean> MV2_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_north"));
    //public static final IUnlistedProperty<Boolean> MV2_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_south"));
    //public static final IUnlistedProperty<Boolean> MV2_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_east"));
    //public static final IUnlistedProperty<Boolean> MV2_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_west"));
    //public static final IUnlistedProperty<Boolean> MV2_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_up"));
    //public static final IUnlistedProperty<Boolean> MV2_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_down"));

    //public static final IUnlistedProperty<Boolean> LV_CORE = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_core"));
    //public static final IUnlistedProperty<Boolean> LV_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_north"));
    //public static final IUnlistedProperty<Boolean> LV_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_south"));
    //public static final IUnlistedProperty<Boolean> LV_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_east"));
    //public static final IUnlistedProperty<Boolean> LV_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_west"));
    //public static final IUnlistedProperty<Boolean> LV_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_up"));
    //public static final IUnlistedProperty<Boolean> LV_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe_down"));

    //public static final IUnlistedProperty<Boolean> LV2_NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_north"));
    //public static final IUnlistedProperty<Boolean> LV2_SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_south"));
    //public static final IUnlistedProperty<Boolean> LV2_EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_east"));
    //public static final IUnlistedProperty<Boolean> LV2_WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_west"));
    //public static final IUnlistedProperty<Boolean> LV2_UP = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_up"));
    //public static final IUnlistedProperty<Boolean> LV2_DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("pipe2_down"));

    //public static final IUnlistedProperty<Boolean> DATA_CORE = new Properties.PropertyAdapter<>(BooleanProperty.create("data_core"));

    public BlockCableTray()
    {
        super(Block.Properties.create(Material.IRON));
        //this.setDefaultState(this.getDefaultState().with(BASE, EnumBaseDirection.NONE));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent("Can place energy Cable and Fluid Pipe in one block"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn.equals(Hand.MAIN_HAND))
        {
            Item item = player.getHeldItemMainhand().getItem();
            Block block = Block.getBlockFromItem(item);
            if (item instanceof ItemPowerScrewDrive || block instanceof BlockFluidPipe || block instanceof BlockEnergyCable)
            {
                TileEntityCableTray te = (TileEntityCableTray) worldIn.getTileEntity(pos);
                if (te != null)
                {
                    boolean change = te.onBlockActivated(player, player.getHeldItemMainhand());
                    if (change) worldIn.notifyBlockUpdate(pos, state, state, 3);
                    return change ? ActionResultType.SUCCESS : ActionResultType.PASS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{BASE}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, PIPE_CORE,
        //        PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST, PIPE_UP, PIPE_DOWN, PIPE2_NORTH, PIPE2_SOUTH, PIPE2_EAST,
        //        PIPE2_WEST, PIPE2_UP, PIPE2_DOWN,
        //        HV_CORE, HV_NORTH, HV_SOUTH, HV_EAST, HV_WEST, HV_UP, HV_DOWN, HV2_NORTH, HV2_SOUTH,
        //        HV2_EAST, HV2_WEST, HV2_UP, HV2_DOWN,
        //        MV_CORE, MV_NORTH, MV_SOUTH, MV_EAST, MV_WEST, MV_UP, MV_DOWN, MV2_NORTH, MV2_SOUTH,
        //        MV2_EAST, MV2_WEST, MV2_UP, MV2_DOWN,
        //        LV_CORE, LV_NORTH, LV_SOUTH, LV_EAST, LV_WEST, LV_UP, LV_DOWN, LV2_NORTH, LV2_SOUTH,
        //        LV2_EAST, LV2_WEST, LV2_UP, LV2_DOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        EnumBaseDirection direction = EnumBaseDirection.NONE;
        Block block = context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite())).getBlock();
        if (!(block instanceof BlockCableTray))
        {
            direction = EnumBaseDirection.byIndex(context.getFace().getOpposite().getIndex());
        }

        return this.getDefaultState().with(BASE, direction);
    }

    @Override
    public boolean canConnectToPipe(IBlockReader worldIn, BlockPos ownPos, Direction neighborDirection)
    {
        BlockState state = worldIn.getBlockState(ownPos.offset(neighborDirection));
        return state.getBlock() instanceof BlockCableTray;
    }

    @Override
    public boolean canConnectToCapability(IBlockReader worldIn, BlockPos ownPos, Direction neighborDirection)
    {
        BlockPos pos = ownPos.offset(neighborDirection);
        BlockState state = worldIn.getBlockState(pos);
        return state.getBlock() instanceof BlockFluidPipe;
    }

    private boolean canConnectToEnergyCapability(IBlockReader worldIn, BlockPos ownPos, Direction neighborDirection, EnumCableIn type)
    {
        BlockPos pos = ownPos.offset(neighborDirection);
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof BlockEnergyCable
                && type.equals(BlockEnergyCable.convertFromType(((BlockEnergyCable) block).type));
    }


    private boolean canConnectFluidPipeTrayToTray(IBlockReader worldIn, BlockPos ownPos, Direction neighborDirection)
    {
        TileEntity otherTE = worldIn.getTileEntity(ownPos.offset(neighborDirection));
        if (otherTE instanceof TileEntityCableTray)
        {
            return ((TileEntityCableTray) otherTE).hasPipe();
        }
        return false;
    }

    private boolean isPipePresent(IBlockReader world, BlockPos pos)
    {
        TileEntityCableTray te = (TileEntityCableTray) world.getTileEntity(pos);
        if (te != null)
        {
            return te.hasPipe();
        }
        return false;
    }

    public boolean isCablePresent(IBlockReader world, BlockPos pos, EnumCableIn type)
    {
        TileEntityCableTray te = (TileEntityCableTray) world.getTileEntity(pos);
        if (te != null)
        {
            return te.getCableIn().equals(type);
        }
        return false;
    }

    private boolean canConnectCableTrayToTray(IBlockReader worldIn, BlockPos ownPos, Direction neighborDirection)
    {
        TileEntity thisTE = worldIn.getTileEntity(ownPos);
        TileEntity otherTE = worldIn.getTileEntity(ownPos.offset(neighborDirection));
        if (thisTE instanceof TileEntityCableTray && otherTE instanceof TileEntityCableTray)
        {
            return ((TileEntityCableTray) thisTE).getCableIn().equals(((TileEntityCableTray) otherTE).getCableIn());
        }
        return false;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    boolean isHvPresent = isCablePresent(world, pos, EnumCableIn.HV);
        //    boolean isMvPresent = isCablePresent(world, pos, EnumCableIn.MV);
        //    boolean isLvPresent = isCablePresent(world, pos, EnumCableIn.LV);
        //    boolean isPipePresent = isPipePresent(world, pos);
        //    return eState.with(MASTER, isMaster(world, pos))
        //            .with(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH)).with(NORTH, canConnectToPipe(world, pos, Direction.NORTH))
        //            .with(EAST, canConnectToPipe(world, pos, Direction.EAST)).with(WEST, canConnectToPipe(world, pos, Direction.WEST))
        //            .with(UP, canConnectToPipe(world, pos, Direction.UP)).with(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
        //            .with(PIPE_CORE, isPipePresent)
        //            .with(PIPE_NORTH, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, Direction.NORTH))
        //            .with(PIPE_SOUTH, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, Direction.SOUTH))
        //            .with(PIPE_EAST, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, Direction.EAST))
        //            .with(PIPE_WEST, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, Direction.WEST))
        //            .with(PIPE_UP, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, Direction.UP))
        //            .with(PIPE_DOWN, isPipePresent && canConnectFluidPipeTrayToTray(world, pos, Direction.DOWN))
        //            .with(PIPE2_NORTH, isPipePresent && canConnectToCapability(world, pos, Direction.NORTH))
        //            .with(PIPE2_SOUTH, isPipePresent && canConnectToCapability(world, pos, Direction.SOUTH))
        //            .with(PIPE2_EAST, isPipePresent && canConnectToCapability(world, pos, Direction.EAST))
        //            .with(PIPE2_WEST, isPipePresent && canConnectToCapability(world, pos, Direction.WEST))
        //            .with(PIPE2_UP, isPipePresent && canConnectToCapability(world, pos, Direction.UP))
        //            .with(PIPE2_DOWN, isPipePresent && canConnectToCapability(world, pos, Direction.DOWN))
        //            .with(HV_CORE, isHvPresent)
        //            .with(HV_NORTH, isHvPresent && canConnectCableTrayToTray(world, pos, Direction.NORTH))
        //            .with(HV_SOUTH, isHvPresent && canConnectCableTrayToTray(world, pos, Direction.SOUTH))
        //            .with(HV_EAST, isHvPresent && canConnectCableTrayToTray(world, pos, Direction.EAST))
        //            .with(HV_WEST, isHvPresent && canConnectCableTrayToTray(world, pos, Direction.WEST))
        //            .with(HV_UP, isHvPresent && canConnectCableTrayToTray(world, pos, Direction.UP))
        //            .with(HV_DOWN, isHvPresent && canConnectCableTrayToTray(world, pos, Direction.DOWN))
        //            .with(HV2_NORTH, isHvPresent && canConnectToEnergyCapability(world, pos, Direction.NORTH, EnumCableIn.HV))
        //            .with(HV2_SOUTH, isHvPresent && canConnectToEnergyCapability(world, pos, Direction.SOUTH, EnumCableIn.HV))
        //            .with(HV2_EAST, isHvPresent && canConnectToEnergyCapability(world, pos, Direction.EAST, EnumCableIn.HV))
        //            .with(HV2_WEST, isHvPresent && canConnectToEnergyCapability(world, pos, Direction.WEST, EnumCableIn.HV))
        //            .with(HV2_UP, isHvPresent && canConnectToEnergyCapability(world, pos, Direction.UP, EnumCableIn.HV))
        //            .with(HV2_DOWN, isHvPresent && canConnectToEnergyCapability(world, pos, Direction.DOWN, EnumCableIn.HV))
        //            .with(MV_CORE, isMvPresent)
        //            .with(MV_NORTH, isMvPresent && canConnectCableTrayToTray(world, pos, Direction.NORTH))
        //            .with(MV_SOUTH, isMvPresent && canConnectCableTrayToTray(world, pos, Direction.SOUTH))
        //            .with(MV_EAST, isMvPresent && canConnectCableTrayToTray(world, pos, Direction.EAST))
        //            .with(MV_WEST, isMvPresent && canConnectCableTrayToTray(world, pos, Direction.WEST))
        //            .with(MV_UP, isMvPresent && canConnectCableTrayToTray(world, pos, Direction.UP))
        //            .with(MV_DOWN, isMvPresent && canConnectCableTrayToTray(world, pos, Direction.DOWN))
        //            .with(MV2_NORTH, isMvPresent && canConnectToEnergyCapability(world, pos, Direction.NORTH, EnumCableIn.MV))
        //            .with(MV2_SOUTH, isMvPresent && canConnectToEnergyCapability(world, pos, Direction.SOUTH, EnumCableIn.MV))
        //            .with(MV2_EAST, isMvPresent && canConnectToEnergyCapability(world, pos, Direction.EAST, EnumCableIn.MV))
        //            .with(MV2_WEST, isMvPresent && canConnectToEnergyCapability(world, pos, Direction.WEST, EnumCableIn.MV))
        //            .with(MV2_UP, isMvPresent && canConnectToEnergyCapability(world, pos, Direction.UP, EnumCableIn.MV))
        //            .with(MV2_DOWN, isMvPresent && canConnectToEnergyCapability(world, pos, Direction.DOWN, EnumCableIn.MV))
        //            .with(LV_CORE, isLvPresent)
        //            .with(LV_NORTH, isLvPresent && canConnectCableTrayToTray(world, pos, Direction.NORTH))
        //            .with(LV_SOUTH, isLvPresent && canConnectCableTrayToTray(world, pos, Direction.SOUTH))
        //            .with(LV_EAST, isLvPresent && canConnectCableTrayToTray(world, pos, Direction.EAST))
        //            .with(LV_WEST, isLvPresent && canConnectCableTrayToTray(world, pos, Direction.WEST))
        //            .with(LV_UP, isLvPresent && canConnectCableTrayToTray(world, pos, Direction.UP))
        //            .with(LV_DOWN, isLvPresent && canConnectCableTrayToTray(world, pos, Direction.DOWN))
        //            .with(LV2_NORTH, isLvPresent && canConnectToEnergyCapability(world, pos, Direction.NORTH, EnumCableIn.LV))
        //            .with(LV2_SOUTH, isLvPresent && canConnectToEnergyCapability(world, pos, Direction.SOUTH, EnumCableIn.LV))
        //            .with(LV2_EAST, isLvPresent && canConnectToEnergyCapability(world, pos, Direction.EAST, EnumCableIn.LV))
        //            .with(LV2_WEST, isLvPresent && canConnectToEnergyCapability(world, pos, Direction.WEST, EnumCableIn.LV))
        //            .with(LV2_UP, isLvPresent && canConnectToEnergyCapability(world, pos, Direction.UP, EnumCableIn.LV))
        //            .with(LV2_DOWN, isLvPresent && canConnectToEnergyCapability(world, pos, Direction.DOWN, EnumCableIn.LV));
        //}
        return state;
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader worldIn, BlockPos pos)
        {
            return FULL_BLOCK_AABB;
        }
    */
    @Nullable
    @Override
    public TileEntityCableTray createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCableTray();
    }
}
