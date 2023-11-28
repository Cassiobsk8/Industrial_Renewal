package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityElectricPump;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class BlockElectricPump extends BlockAbstractHorizontalFacing implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 1);

    public BlockElectricPump(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(INDEX, 1));
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(world.getBlockState(pos.relative(context.getHorizontalDirection())).canBeReplaced())
            return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection())
                    .setValue(INDEX, 0);
        return null;
//        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
        if (state.getValue(INDEX) == 0)
            world.setBlockAndUpdate(pos.relative(state.getValue(FACING)), state.setValue(INDEX, 1));
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) {
            super.destroy(world, pos, state);
            return;
        }
        switch (state.getValue(INDEX))
        {
            case 0:
                if (IsPump(world, pos.relative(state.getValue(FACING))))
                    world.removeBlock(pos.relative(state.getValue(FACING)), false);
                break;
            case 1:
                if (IsPump(world, pos.relative(state.getValue(FACING).getOpposite())))
                    world.removeBlock(pos.relative(state.getValue(FACING).getOpposite()), false);
                break;
        }
        popResource((Level) world, pos, new ItemStack(this.asItem()));
        super.destroy(world, pos, state);
    }

    private boolean IsPump(LevelAccessor world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricPump;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(INDEX);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.ELECTRIC_PUMP_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityElectricPump)blockEntity).tick();
    }
}
