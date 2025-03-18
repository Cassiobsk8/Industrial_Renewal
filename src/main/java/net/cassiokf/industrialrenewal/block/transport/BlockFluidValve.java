package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeSwitchBase;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityFluidValve;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockFluidValve extends BlockPipeSwitchBase implements EntityBlock {
    public BlockFluidValve(Properties property) {
        super(property);
    }
    
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityFluidValve(pos, state);
    }
}
