package net.cassiokf.industrialrenewal.block.decor;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.init.ModBlocks;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockCatwalkStair extends BlockAbstractHorizontalFacing {

    public static final BooleanProperty ACTIVE_LEFT = BooleanProperty.create("active_left");
    public static final BooleanProperty ACTIVE_RIGHT = BooleanProperty.create("active_right");

    protected static final VoxelShape BASE_AABB = Block.box(0, 0, 0, 16, 8, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 8, 0, 16, 16, 8);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 8, 8, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 8, 0, 8, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.box(8, 8, 0, 16, 16, 16);

    protected static final VoxelShape NC_AABB = Block.box(0, 0, 0, 16, 32, 0.5);
    protected static final VoxelShape SC_AABB = Block.box(0, 0, 15.5, 16, 32, 16);
    protected static final VoxelShape WC_AABB = Block.box(0, 0, 0, 0.5, 32, 16);
    protected static final VoxelShape EC_AABB = Block.box(15.5, 0, 0, 16, 32, 16);

    protected static final VoxelShape RNC_AABB = Block.box(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape RSC_AABB = Block.box(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape RWC_AABB = Block.box(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape REC_AABB = Block.box(15.5, 0, 0, 16, 16, 16);

    public BlockCatwalkStair(Properties properties) {
        super(properties);
    }

    public BlockCatwalkStair()
    {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() instanceof ItemBlockCatwalkStair || context.getItemInHand().getItem() instanceof ItemBlockCatwalk;
        return super.canBeReplaced(state, context);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            if (handIn == InteractionHand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.SCREW_DRIVER.get())) {
                    state = state.cycle(FACING);
                    worldIn.setBlockAndUpdate(pos, state);
                    return InteractionResult.SUCCESS;
                }
                BlockPos posOffset = pos.relative(state.getValue(FACING)).above();
                BlockState stateOffset = worldIn.getBlockState(posOffset);

                BlockCatwalkStair catwalk_stair = playerItem.equals(ModBlocks.CATWALK_STAIR.get().asItem()) ? ModBlocks.CATWALK_STAIR.get() : (playerItem.equals(ModBlocks.CATWALK_STAIR_STEEL.get().asItem()) ? ModBlocks.CATWALK_STAIR_STEEL.get() : null);
                BlockCatwalk catwalk = playerItem.equals(ModBlocks.CATWALK.get().asItem()) ? ModBlocks.CATWALK.get() : (playerItem.equals(ModBlocks.CATWALK_STEEL.get().asItem()) ? ModBlocks.CATWALK_STEEL.get() : null);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE_LEFT, ACTIVE_RIGHT);

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction facing = context.getHorizontalDirection();
        BlockState state = defaultBlockState().setValue(FACING, facing);
        return state.setValue(ACTIVE_LEFT, sideConnected(state, context.getLevel(), context.getClickedPos(), facing.getCounterClockWise()))
                .setValue(ACTIVE_RIGHT, sideConnected(state, context.getLevel(), context.getClickedPos(), facing.getClockWise()));
    }

    public BlockState getStateForPlacement(Level level, BlockPos pos, Direction facing)
    {
        BlockState state = defaultBlockState().setValue(FACING, facing);
        return state.setValue(ACTIVE_LEFT, sideConnected(state, level, pos, facing.getCounterClockWise()))
                .setValue(ACTIVE_RIGHT, sideConnected(state, level, pos, facing.getClockWise()));
    }

    private Boolean sideConnected(BlockState state, Level world, BlockPos pos, Direction direction)
    {
        Direction face = state.getValue(FACING);
        BlockPos posOffset = pos.relative(direction);
        BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            Direction sideStairFace = stateOffset.getValue(FACING);
            return !(sideStairFace == face);
        }
        return true;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        VoxelShape FINAL_SHAPE = BASE_AABB;

        Direction face = state.getValue(FACING);
        Boolean left = state.getValue(ACTIVE_LEFT);
        Boolean right = state.getValue(ACTIVE_RIGHT);
        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, NORTH_AABB);
        }
        if (face == Direction.SOUTH) {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, SOUTH_AABB);
        }
        if (face == Direction.WEST) {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, WEST_AABB);
        }
        if (face == Direction.EAST) {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, EAST_AABB);
        }
        return FINAL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state, false);
    }

    private VoxelShape getVoxelShape(BlockState state, boolean isForRender)
    {
        VoxelShape FINAL_SHAPE = BASE_AABB;

        Direction face = state.getValue(FACING);
        Boolean left = state.getValue(ACTIVE_LEFT);
        Boolean right = state.getValue(ACTIVE_RIGHT);
        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, NORTH_AABB);
            if (left)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? RWC_AABB : WC_AABB);
            }
            if (right)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? REC_AABB : EC_AABB);
            }

        }
        if (face == Direction.SOUTH) {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, SOUTH_AABB);
            if (left) {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? REC_AABB : EC_AABB);
            }
            if (right) {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? RWC_AABB : WC_AABB);
            }
        }
        if (face == Direction.WEST) {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, WEST_AABB);
            if (left) {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? RSC_AABB : SC_AABB);
            }
            if (right) {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? RNC_AABB : NC_AABB);
            }
        }
        if (face == Direction.EAST) {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, EAST_AABB);
            if (left)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? RNC_AABB : NC_AABB);
            }
            if (right)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, isForRender ? RSC_AABB : SC_AABB);
            }
        }
        return FINAL_SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        state = state.setValue(ACTIVE_LEFT, sideConnected(state, world, pos, state.getValue(FACING).getCounterClockWise()))
                .setValue(ACTIVE_RIGHT, sideConnected(state, world, pos, state.getValue(FACING).getClockWise()));
        world.setBlock(pos, state, 2);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }


}
