package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacingWithActivating;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockCatwalkLadder extends BlockAbstractHorizontalFacingWithActivating {

    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    protected static final VoxelShape DOWN_AABB = Block.box(0, 0, 0, 16, 0.5, 16);
    protected static final VoxelShape LADDER_EAST_AABB = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape LADDER_WEST_AABB = Block.box(13, 0, 0, 16, 16, 16);
    protected static final VoxelShape LADDER_SOUTH_AABB = Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape LADDER_NORTH_AABB = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15.5, 0, 0, 16, 16, 16);

    public BlockCatwalkLadder(Properties properties) {
        super(properties.strength(1f));
    }

    public BlockCatwalkLadder()
    {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f));
        registerDefaultState(defaultBlockState().setValue(DOWN, false).setValue(ACTIVE, true));
    }

    @Override
    public boolean canBeReplaced(BlockState p_196253_1_, BlockPlaceContext context) {
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() == this.asItem();
        return super.canBeReplaced(p_196253_1_, context);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide) {
            if (handIn == InteractionHand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.SCREW_DRIVER.get())) {
                    return super.use(state, worldIn, pos, player, handIn, hitResult);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DOWN);

    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    private boolean downConnection(Level world, BlockPos pos)
    {
        Block downB = world.getBlockState(pos.below()).getBlock();
        return !(downB instanceof LadderBlock
                || downB instanceof BlockCatwalkLadder
                || downB instanceof BlockCatwalkHatch
                || downB instanceof BlockCatwalkStair
                || downB instanceof StairBlock
                || downB instanceof TrapDoorBlock);
    }

    protected boolean openCageIf(final Level worldIn, BlockPos ownPos)
    {
        final BlockPos downpos = ownPos.below();
        final BlockPos twoDownPos = downpos.below();
        final BlockState downState = worldIn.getBlockState(downpos);
        final BlockState twoDownState = worldIn.getBlockState(twoDownPos);
        return !downState.isFaceSturdy(worldIn, downpos, Direction.UP)
                && (!(downState.getBlock() instanceof BlockCatwalkLadder)
                || downState.getValue(ACTIVE)
                || !twoDownState.isFaceSturdy(worldIn, twoDownPos, Direction.UP));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ACTIVE, openCageIf(context.getLevel(), context.getClickedPos()))
                .setValue(DOWN, downConnection(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        //Utils.debug("neighbor changed", state, world, pos, block, neighbor, flag);
        state = state
                .setValue(FACING, state.getValue(FACING))
                .setValue(ACTIVE, openCageIf(world, pos))
                .setValue(DOWN, downConnection(world, pos));
        world.setBlockAndUpdate(pos, state);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        Direction face = state.getValue(FACING);
        boolean active = state.getValue(ACTIVE);
        boolean down = state.getValue(DOWN);

        VoxelShape FINAL_SHAPE = NULL_SHAPE;

        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, LADDER_SOUTH_AABB);
            if (active)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, EAST_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        else if (face == Direction.SOUTH)
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, LADDER_NORTH_AABB);
            if (active)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, EAST_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        else if (face == Direction.WEST)
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, LADDER_EAST_AABB);
            if (active)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, EAST_AABB);
            }
        }
        else
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, LADDER_WEST_AABB);
            if (active)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        if (down)
        {
            FINAL_SHAPE = Shapes.or(FINAL_SHAPE, DOWN_AABB);
        }
        return FINAL_SHAPE;
    }


}
