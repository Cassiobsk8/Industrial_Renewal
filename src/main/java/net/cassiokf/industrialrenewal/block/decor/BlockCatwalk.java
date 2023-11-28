package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractSixWayConnections;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.item.decor.ItemBlockCatwalk;
import net.cassiokf.industrialrenewal.item.decor.ItemBlockCatwalkStair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class BlockCatwalk extends BlockAbstractSixWayConnections {

    protected static final VoxelShape BASE_AABB = Block.box(0, 0, 0, 16, 2, 16);

    protected static final VoxelShape RNORTH_AABB = Block.box(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape RSOUTH_AABB = Block.box(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.box(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.box(15.5, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 24, 0.5);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15.5, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 0.5, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15.5, 0, 0, 16, 24, 16);

    public BlockCatwalk()
    {
        super(metalBasicProperties.speedFactor(1.2f).strength(1f), 16, 2);
    }


    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(direction), canConnectTo(context.getLevel(), context.getClickedPos(), direction));
        }
        return state;
    }

    public BlockState getStateForPlacement(Level level, BlockPos pos) {
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(direction), canConnectTo(level, pos, direction));
        }
        return state;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext context) {
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() instanceof ItemBlockCatwalk || context.getItemInHand().getItem() instanceof ItemBlockCatwalkStair;
        //context.getItemInHand().getItem() == this.asItem();// || Block.byItem(context.getItemInHand().getItem()) instanceof BlockCatwalkStair;
        return super.canBeReplaced(blockState, context);
    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if(!worldIn.isClientSide){
            if (handIn == InteractionHand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.SCREW_DRIVER.get())) {
                    Vec3 hitQuad = hit.getLocation().subtract(Vec3.atCenterOf(pos));
                    if (hit.getDirection() == Direction.UP)
                        state = state.cycle(quadToDir(hitQuad));
                    else
                        state = state.cycle(getBooleanProperty(hit.getDirection()));

                    worldIn.setBlock(pos, state, 2);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public BooleanProperty getBooleanProperty(Direction face){
        switch (face){
            default:
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case UP: return UP;
            case DOWN: return DOWN;
        }
    }

    public BooleanProperty quadToDir(Vec3 vector3d){
        if(vector3d.z > vector3d.x && vector3d.z > -vector3d.x)
            return SOUTH;
        if(vector3d.z < vector3d.x && vector3d.z < -vector3d.x)
            return NORTH;
        if(vector3d.z > vector3d.x && vector3d.z < -vector3d.x)
            return WEST;
        if(vector3d.z < vector3d.x && vector3d.z > -vector3d.x)
            return EAST;
        return NORTH;
    }

    protected boolean isValidConnection(final BlockState neighborState, final BlockGetter world, final BlockPos ownPos, final Direction neighborDirection)
    {
        BlockState downstate = world.getBlockState(ownPos.relative(neighborDirection).below());
        Block nb = neighborState.getBlock();

        if (neighborDirection == Direction.DOWN)
        {
            return nb instanceof BlockCatwalkLadder
                    || nb instanceof LadderBlock
                    || nb instanceof BlockCatwalk;
        }
        
        if (neighborDirection == Direction.UP) {
//            return !(neighborState.getBlock() instanceof BlockEnergyCable);
            return true;
        }
        
        return nb instanceof BlockCatwalk
                || nb instanceof DoorBlock
                || nb instanceof BlockElectricGate
                || (nb instanceof StairBlock && (neighborState.getValue(StairBlock.FACING) == neighborDirection || neighborState.getValue(StairBlock.FACING) == neighborDirection.getOpposite()))
                || (downstate.getBlock() instanceof StairBlock && downstate.getValue(StairBlock.FACING) == neighborDirection.getOpposite())
                || (nb instanceof BlockCatwalkHatch && neighborState.getValue(BlockCatwalkHatch.FACING) == neighborDirection)
                || (nb instanceof BlockCatwalkGate && neighborState.getValue(BlockCatwalkGate.FACING) == neighborDirection.getOpposite())
                || (nb instanceof BlockCatwalkStair && neighborState.getValue(BlockCatwalkStair.FACING) == neighborDirection)
                || (downstate.getBlock() instanceof BlockCatwalkStair && downstate.getValue(BlockCatwalkStair.FACING) == neighborDirection.getOpposite())
                || (downstate.getBlock() instanceof BlockCatwalkLadder && downstate.getValue(BlockCatwalkLadder.FACING) == neighborDirection.getOpposite())
                || (nb instanceof BlockCatwalkLadder && neighborState.getValue(BlockCatwalkLadder.FACING) == neighborDirection && !neighborState.getValue(BlockCatwalkLadder.ACTIVE));
    }

    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.getValue(getPropertyBasedOnDirection(facing));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        VoxelShape SHAPE = NULL_SHAPE;
        SHAPE = Shapes.or(SHAPE, BASE_AABB);
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        VoxelShape SHAPE = NULL_SHAPE;
        if (isConnected(state, Direction.DOWN))
        {
            SHAPE = Shapes.or(SHAPE, BASE_AABB);
        }
        if (isConnected(state, Direction.NORTH))
        {
            SHAPE = Shapes.or(SHAPE, NORTH_AABB);
        }
        if (isConnected(state, Direction.SOUTH))
        {
            SHAPE = Shapes.or(SHAPE, SOUTH_AABB);
        }
        if (isConnected(state, Direction.WEST))
        {
            SHAPE = Shapes.or(SHAPE, WEST_AABB);
        }
        if (isConnected(state, Direction.EAST))
        {
            SHAPE = Shapes.or(SHAPE, EAST_AABB);
        }
        return SHAPE;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, 2);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }
}
