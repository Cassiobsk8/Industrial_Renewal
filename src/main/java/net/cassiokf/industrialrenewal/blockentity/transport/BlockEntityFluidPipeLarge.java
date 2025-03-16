package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityFluidPipeBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFluidPipeLarge extends BlockEntityFluidPipeBase<BlockEntityFluidPipeLarge> {

    public BlockEntityFluidPipeLarge(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUIDPIPE_LARGE_TILE.get(), pos, state, 20000);
    }

    public void tick(){
        super.tick();
    }

//    public TileEntityFluidPipe(TileEntityType<?> tileEntityTypeIn) {
//        super(tileEntityTypeIn);
//    }
    
    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityFluidPipeLarge;// || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }
}
