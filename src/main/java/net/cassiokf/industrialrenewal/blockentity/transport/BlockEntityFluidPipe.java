package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityFluidPipeBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFluidPipe extends BlockEntityFluidPipeBase<BlockEntityFluidPipe> {
    public static int MAX_TRANSFER = 1000;
    
    public BlockEntityFluidPipe(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUIDPIPE_TILE.get(), pos, state, MAX_TRANSFER);
    }
    
    public BlockEntityFluidPipe(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, MAX_TRANSFER);
    }
    
    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityFluidPipe;// || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }
}
