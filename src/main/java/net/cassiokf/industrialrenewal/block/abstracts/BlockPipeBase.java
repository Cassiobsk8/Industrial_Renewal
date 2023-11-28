package net.cassiokf.industrialrenewal.block.abstracts;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockPipeBase<TE extends BlockEntityMultiBlocksTube> extends BlockConnectedMultiblocks<TE> {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty FLOOR = BooleanProperty.create("floor");
    public static final BooleanProperty UPFLOOR = BooleanProperty.create("upfloor");
    public static final BooleanProperty CSOUTH = BooleanProperty.create("c_south");
    public static final BooleanProperty CNORTH = BooleanProperty.create("c_north");
    public static final BooleanProperty CEAST = BooleanProperty.create("c_east");
    public static final BooleanProperty CWEST = BooleanProperty.create("c_west");
    public static final BooleanProperty CUP = BooleanProperty.create("c_up");
    public static final BooleanProperty CDOWN = BooleanProperty.create("c_down");

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static double UP2 = 16;
    private static double DOWN1 = 0;

    public float nodeWidth;
    public float nodeHeight;

    public BlockPipeBase(Block.Properties property, float nodeWidth, float nodeHeight)
    {
        super(property.strength(0.8f));
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(FLOOR, false)
                .setValue(UPFLOOR, false)
                .setValue(CSOUTH, false)
                .setValue(CNORTH, false)
                .setValue(CEAST, false)
                .setValue(CWEST, false)
                .setValue(CUP, false)
                .setValue(CDOWN, false));
    }

    public boolean isMaster(Level world, BlockPos pos)
    {
        BlockEntityMultiBlocksTube te = (BlockEntityMultiBlocksTube) world.getBlockEntity(pos);
        return te != null && te.isMaster();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, FLOOR, UPFLOOR, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        ItemStack playerStack = player.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            BlockEntity be = worldIn.getBlockEntity(pos);
            if (be instanceof BlockEntityMultiBlocksTube<?> bet) {
                Utils.sendChatMessage(player, "Valid outputs: " +bet.getMaster().getReceiversContainers().size());
                
            }
        }
        if(playerStack.getItem().equals(ModBlocks.INDUSTRIAL_FLOOR.get().asItem()) && !state.getValue(FLOOR)){
            state = state.setValue(FLOOR, true);
            BlockState stateAbove = worldIn.getBlockState(pos.above());
            if(stateAbove.getBlock() instanceof BlockPipeBase) {
                worldIn.setBlock(pos, state.setValue(UPFLOOR, stateAbove.getValue(FLOOR)), 3);
            }
            else{
                worldIn.setBlock(pos, state.setValue(UPFLOOR, false), 3);
            }
            if (!player.isCreative())
                playerStack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        if(playerStack.getItem().equals(ModItems.SCREW_DRIVER.get()) && state.getValue(FLOOR)){
            state = state.setValue(FLOOR, false);
            //            BlockState stateAbove = worldIn.getBlockState(pos.above());
            worldIn.setBlock(pos, state.setValue(UPFLOOR, false), 3);
            Block.popResource(worldIn, pos, new ItemStack(ModBlocks.INDUSTRIAL_FLOOR.get()));
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), canConnectToPipe(worldIn, pos, direction)), 3);
                worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanPropCap(direction), canConnectToCapability(worldIn, pos, direction)), 3);
            }
            BlockEntity be = worldIn.getBlockEntity(pos);
            if (be instanceof BlockEntityMultiBlocksTube<?> mb) {
                mb.checkForOutPuts(fromPos);
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BlockEntityMultiBlocksTube<?>) {
                ((BlockEntityMultiBlocksTube)blockentity).breakBlock();
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, flag);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (isConnected(worldIn, pos, Direction.NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = 8 + (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = 8 + (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.DOWN))
        {
            DOWN1 = 0;
        } else
        {
            DOWN1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.UP))
        {
            UP2 = 16;
        } else
        {
            UP2 = 8 + (nodeWidth / 2);
        }
        return Block.box(WESTX1, DOWN1, NORTHZ1, EASTX2, UP2, SOUTHZ2);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return true;
    }

    public final boolean isConnected(BlockGetter worldIn, BlockPos pos, Direction facing)
    {
        if(worldIn.getBlockState(pos).getBlock() instanceof BlockPipeBase) {
            BlockState state = worldIn.getBlockState(pos);
            return state.getValue(directionToBooleanProp(facing))
                    || state.getValue(directionToBooleanPropCap(facing));
        }
        return false;
    }

    public BooleanProperty directionToBooleanProp(Direction d){
        switch (d){
            case UP: return UP;
            case DOWN: return DOWN;
            default:
            case NORTH: return NORTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case SOUTH: return SOUTH;
        }
    }
    
    public BooleanProperty directionToBooleanPropCap(Direction d){
        switch (d){
            case UP: return CUP;
            case DOWN: return CDOWN;
            default:
            case NORTH: return CNORTH;
            case EAST: return CEAST;
            case WEST: return CWEST;
            case SOUTH: return CSOUTH;
        }
    }

    public abstract boolean canConnectToPipe(BlockGetter world, BlockPos pos, Direction facing);
    public abstract boolean canConnectToCapability(BlockGetter world, BlockPos pos, Direction facing);
}
