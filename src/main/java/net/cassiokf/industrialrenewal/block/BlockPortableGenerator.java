package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockSaveContent;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityPortableGenerator;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockPortableGenerator extends BlockSaveContent implements EntityBlock {
    
    public BlockPortableGenerator(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    
    public BlockPortableGenerator() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2f));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult rayTraceResult) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BlockEntityPortableGenerator g) {
            if (entity.isCrouching()) {
                g.changeGenerator();
            }
        }
        return super.use(state, world, pos, entity, hand, rayTraceResult);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.PORTABLE_GENERATOR_TILE.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide ? null : ($0, $1, $2, blockEntity) -> ((BlockEntityPortableGenerator) blockEntity).tick();
    }
}
