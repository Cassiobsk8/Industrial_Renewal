package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockConnectedMultiblocks;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbinePillar;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockWindTurbinePillar extends BlockConnectedMultiblocks<BlockEntityWindTurbinePillar> implements EntityBlock {
    public static final BooleanProperty BASE = BooleanProperty.create("base");

    public BlockWindTurbinePillar(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(BASE, false));
    }

    public BlockWindTurbinePillar() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(BASE, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        Item playerItem = player.getMainHandItem().getItem();
        Block clickedBlock = state.getBlock();
        if (playerItem.equals(ModBlocks.TURBINE_PILLAR.get().asItem()) && clickedBlock.equals(ModBlocks.TURBINE_PILLAR.get())) {
            int n = 1;
            while (worldIn.getBlockState(pos.above(n)).getBlock() instanceof BlockWindTurbinePillar) {
                n++;
            }
            if (worldIn.getBlockState(pos.above(n)).canBeReplaced()) {
                BlockState aboveState = state.setValue(FACING, state.getValue(FACING)).setValue(BASE, false);
                worldIn.playSound(player, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS);
                if (!worldIn.isClientSide()) worldIn.setBlockAndUpdate(pos.above(n), aboveState);
                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BASE);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        boolean value = !level.getBlockState(pos.below()).getBlock().equals(ModBlocks.TURBINE_PILLAR.get());
        return super.getStateForPlacement(context).setValue(BASE, value);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide() && fromPos.equals(pos.below())) {
            boolean value = !(worldIn.getBlockEntity(pos.below()) instanceof BlockEntityWindTurbinePillar);

            state = state.setValue(BASE, value);
            worldIn.setBlockAndUpdate(pos, state);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.TURBINE_PILLAR_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityWindTurbinePillar)blockEntity).tick();
    }
}
