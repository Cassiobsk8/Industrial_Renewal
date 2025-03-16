package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.blockentity.transport.BEFluidPipeGauge;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockFluidPipeGauge extends BlockFluidPipe {
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if (player.getItemInHand(handIn).getItem() instanceof ItemScrewdriver) {
            if (!worldIn.isClientSide) {
                BlockFluidPipe block = ModBlocks.FLUID_PIPE.get();
                worldIn.setBlockAndUpdate(pos, block.getState(worldIn, pos, block.defaultBlockState()));
                if (!player.isCreative())
                    player.addItem(new ItemStack(ModBlocks.FLUID_GAUGE.get()));
                ItemScrewdriver.playSound(worldIn, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.FLUIDPIPE_GAUGE_TILE.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BEFluidPipeGauge)blockEntity).tick();
    }
}
