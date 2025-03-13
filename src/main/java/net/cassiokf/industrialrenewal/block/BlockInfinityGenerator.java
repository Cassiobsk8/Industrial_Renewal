package net.cassiokf.industrialrenewal.block;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractNotFullCube;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityInfinityGenerator;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockInfinityGenerator extends BlockAbstractNotFullCube implements EntityBlock {
    public BlockInfinityGenerator()
    {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.INFINITY_GENERATOR.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : ($0, $1, $2, blockEntity) -> ((BlockEntityInfinityGenerator)blockEntity).tick();
    }
}
