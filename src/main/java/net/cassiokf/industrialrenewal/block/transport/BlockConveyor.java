package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityConveyor;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.util.enums.EnumConveyorTier;
import net.cassiokf.industrialrenewal.util.enums.EnumConveyorType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockConveyor extends BlockAbstractHorizontalFacing implements EntityBlock {

    public static final IntegerProperty MODE = IntegerProperty.create("mode", 0, 2);

    public static final EnumProperty<EnumConveyorTier> TIER = EnumProperty.create("tier", EnumConveyorTier.class);
    public static final EnumProperty<EnumConveyorType> TYPE = EnumProperty.create("type", EnumConveyorType.class);
    public static final BooleanProperty FRONT = BooleanProperty.create("front");
    public static final BooleanProperty BACK = BooleanProperty.create("back");

    protected static final VoxelShape BASE_AABB = Block.box(0, 0, 0, 16, 8, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 8, 0, 16, 16, 8);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 8, 8, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 8, 0, 8, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.box(8, 8, 0, 16, 16, 16);

    public BlockConveyor(EnumConveyorType type) {
        super(metalBasicProperties);
        registerDefaultState(defaultBlockState().setValue(TYPE, type));
    }

    public BlockConveyor(EnumConveyorTier tier) {
        super(metalBasicProperties.strength(1f));
        registerDefaultState(defaultBlockState().setValue(TIER, tier));
    }

    public BlockConveyor(EnumConveyorType type, EnumConveyorTier tier) {
        super(metalBasicProperties);
        registerDefaultState(defaultBlockState().setValue(TYPE, type).setValue(TIER, tier));
    }

    public static double getMotionX(Direction facing)
    {
        return facing == Direction.EAST ? 0.2 : -0.2;
    }

    public static double getMotionZ(Direction facing)
    {
        return facing == Direction.SOUTH ? 0.2 : -0.2;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODE, FRONT, BACK, TIER, TYPE);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_60472_, BlockGetter p_60473_, BlockPos p_60474_) {
        return 1.0f;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean p_220069_6_) {
        if(!world.isClientSide){
            int mode = getMode(world, pos, state);
            world.setBlock(pos, state.setValue(MODE, mode).setValue(FRONT, getFront(world, pos, state, mode)).setValue(BACK, getBack(world, pos, state, mode)), 3);
        }
        super.neighborChanged(state, world, pos, block, neighborPos, p_220069_6_);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && handIn.equals(handIn.MAIN_HAND))
        {
            if (state.getValue(TYPE) == EnumConveyorType.NORMAL && state.getValue(MODE) == 0)
            {
                if (heldItem.getItem().equals(Blocks.HOPPER.asItem()))
                {
//                    Direction facing1 = state.getValue(FACING);
                    state = state.setValue(TYPE, EnumConveyorType.HOPPER);
                    worldIn.setBlock(pos, state, 3);
                    worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return InteractionResult.SUCCESS;
                }
                if (heldItem.getItem().equals(Blocks.DROPPER.asItem()))
                {
//                    Direction facing1 = state.getValue(FACING);
                    state = state.setValue(TYPE, EnumConveyorType.INSERTER);
                    worldIn.setBlock(pos, state, 3);
                    worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            else if (heldItem.getItem().equals(ModItems.SCREW_DRIVER.get()))
            {
                if (state.getValue(TYPE) == EnumConveyorType.HOPPER)
                {
                    Direction facing1 = state.getValue(FACING);
                    state = state.setValue(TYPE, EnumConveyorType.NORMAL);
                    popResource(worldIn, pos, new ItemStack(Items.HOPPER));
                    worldIn.setBlock(pos, state, 3);
//                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    return InteractionResult.SUCCESS;
                }
                if (state.getValue(TYPE) == EnumConveyorType.INSERTER)
                {
                    Direction facing1 = state.getValue(FACING);
                    state = state.setValue(TYPE, EnumConveyorType.NORMAL);
                    popResource(worldIn, pos, new ItemStack(Items.DROPPER));
                    worldIn.setBlock(pos, state, 3);
//                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    return InteractionResult.SUCCESS;
                }
            } else if (Block.byItem(heldItem.getItem()) instanceof BlockConveyor)
            {
                Direction face = state.getValue(FACING);
                int mode = state.getValue(MODE);
                if (mode == 2 && worldIn.getBlockState(pos.relative(face).below()).canBeReplaced())
                {
                    if (!worldIn.isClientSide)
                    {
                        worldIn.setBlock(pos.relative(face).below(), state, 3);
                        if (!player.isCreative()) heldItem.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (worldIn.getBlockState(pos.relative(face)).canBeReplaced())
                {
                    if (!worldIn.isClientSide)
                    {
                        worldIn.setBlock(pos.relative(face), state, 3);
                        if (!player.isCreative()) heldItem.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }


    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateIn = defaultBlockState();
        int mode = getMode(context.getLevel(), context.getClickedPos(), stateIn);
        boolean front = getFront(context.getLevel(), context.getClickedPos(), stateIn, mode);
        boolean back = getBack(context.getLevel(), context.getClickedPos(), stateIn, mode);

        return stateIn.setValue(FACING, context.getHorizontalDirection())
                .setValue(MODE, mode).setValue(FRONT, front).setValue(BACK, back);
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);

        switch (state.getValue(TYPE)){
            case HOPPER:
                popResource((Level) world, pos, new ItemStack(Items.HOPPER));
                break;
            case INSERTER:
                popResource((Level)world, pos, new ItemStack(Items.DROPPER));
                break;
        }

        BlockPos updatePos = pos.above();
        BlockPos updatePos2 = pos.below();
        ((Level)world).sendBlockUpdated(updatePos, world.getBlockState(updatePos), world.getBlockState(updatePos), 3);
        ((Level)world).sendBlockUpdated(updatePos2, world.getBlockState(updatePos2), world.getBlockState(updatePos2), 3);

        ((Level)world).updateNeighborsAt(updatePos, world.getBlockState(updatePos).getBlock());
        ((Level)world).updateNeighborsAt(updatePos2, world.getBlockState(updatePos2).getBlock());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);

        BlockPos updatePos = pos.above();
        BlockPos updatePos2 = pos.below();
        world.sendBlockUpdated(updatePos, world.getBlockState(updatePos), world.getBlockState(updatePos), 3);
        world.sendBlockUpdated(updatePos2, world.getBlockState(updatePos2), world.getBlockState(updatePos2), 3);

        world.updateNeighborsAt(updatePos, world.getBlockState(updatePos).getBlock());
        world.updateNeighborsAt(updatePos2, world.getBlockState(updatePos2).getBlock());
    }

    private int getMode(Level world, BlockPos pos, BlockState ownState)
    {
        if (ownState.getValue(TYPE) != EnumConveyorType.NORMAL) return 0;
        Direction facing = ownState.getValue(FACING);
        BlockState frontState = world.getBlockState(pos.relative(facing));
        BlockState upState = world.getBlockState(pos.relative(facing).above());
        BlockState directUpState = world.getBlockState(pos.above());
        BlockState downState = world.getBlockState(pos.relative(facing).below());
        BlockState backUpState = world.getBlockState(pos.relative(facing.getOpposite()).above());
        BlockState backState = world.getBlockState(pos.relative(facing.getOpposite()));

        //if (frontState.getBlock() instanceof BlockBulkConveyor && frontState.get(FACING) == facing) return 0;
        if ((upState.getBlock() instanceof BlockConveyor &&
                upState.getValue(FACING).equals(facing)) &&
                !(directUpState.getBlock() instanceof BlockConveyor) &&
                !(frontState.getBlock() instanceof BlockConveyor &&
                        frontState.getValue(FACING).equals(facing)))

            return 1;
        if ((downState.getBlock() instanceof BlockConveyor && downState.getValue(FACING).equals(facing)
                && backUpState.getBlock() instanceof BlockConveyor && backUpState.getValue(FACING).equals(facing))
                || (!(backState.getBlock() instanceof BlockConveyor && backState.getValue(FACING).equals(facing))
                && (backUpState.getBlock() instanceof BlockConveyor && backUpState.getValue(FACING).equals(facing))))
            return 2;
        return 0;
    }

    private boolean getFront(Level world, BlockPos pos, BlockState ownState, final int mode)
    {
        if (ownState.getValue(TYPE).equals(EnumConveyorType.INSERTER)) return false;

        Direction facing = ownState.getValue(FACING);
        BlockState frontState = world.getBlockState(pos.relative(facing));
        BlockState downState = world.getBlockState(pos.relative(facing).below());
        BlockState aboveState = world.getBlockState(pos.relative(facing).above());

        return !(frontState.getBlock() instanceof BlockConveyor || downState.getBlock() instanceof BlockConveyor || aboveState.getBlock() instanceof BlockConveyor);
    }

    private boolean getBack(Level world, BlockPos pos, BlockState ownState, final int mode)
    {
        Direction facing = ownState.getValue(FACING);
        BlockState backState = world.getBlockState(pos.relative(facing.getOpposite()));
        BlockState downState = world.getBlockState(pos.relative(facing.getOpposite()).below());

        if (mode == 0)
            return !(backState.getBlock() instanceof BlockConveyor && backState.getValue(FACING).equals(facing)) && !(downState.getBlock() instanceof BlockConveyor && downState.getValue(FACING).equals(facing));
        if (mode == 1)
            return !(downState.getBlock() instanceof BlockConveyor && downState.getValue(FACING).equals(facing)) && !(backState.getBlock() instanceof BlockConveyor && backState.getValue(FACING).equals(facing));
        if (mode == 2) return false;
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        VoxelShape FINAL_SHAPE = NULL_SHAPE;
        Direction face = state.getValue(FACING);
        int mode = state.getValue(MODE);
        boolean ramp = mode == 1 || mode == 2;
        if (state.getValue(TYPE) == EnumConveyorType.NORMAL)
        {
            FINAL_SHAPE = Shapes.join(FINAL_SHAPE, BASE_AABB, BooleanOp.OR);
            if (ramp)
            {
                switch (face)
                {
                    case NORTH:
                        FINAL_SHAPE = Shapes.join(FINAL_SHAPE, NORTH_AABB, BooleanOp.OR);
                    case SOUTH:
                        FINAL_SHAPE = Shapes.join(FINAL_SHAPE, SOUTH_AABB, BooleanOp.OR);
                    case WEST:
                        FINAL_SHAPE = Shapes.join(FINAL_SHAPE, WEST_AABB, BooleanOp.OR);
                    case EAST:
                        FINAL_SHAPE = Shapes.join(FINAL_SHAPE, EAST_AABB, BooleanOp.OR);
                }
            }
        } else
        {
            FINAL_SHAPE = FULL_SHAPE;
        }
        return FINAL_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.CONVEYOR_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityConveyor)blockEntity).tick();
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BlockEntityConveyor) {
                ((BlockEntityConveyor)blockentity).dropContents();
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, flag);
        }
    }
}
