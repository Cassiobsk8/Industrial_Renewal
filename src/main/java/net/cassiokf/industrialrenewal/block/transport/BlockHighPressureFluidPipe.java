package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeBase;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityHighPressureFluidPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

public class BlockHighPressureFluidPipe extends BlockPipeBase<BlockEntityHighPressureFluidPipe> implements EntityBlock {
    public BlockHighPressureFluidPipe() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), 8, 8);
    }
    
    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos(), defaultBlockState());
    }
    
    //    @Override
    //    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    //        if(!worldIn.isClientSide){
    //            for(Direction direction : Direction.values()){
    //                if(canConnectTo(worldIn, pos, direction)){
    //                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), Constants.BlockFlags.DEFAULT);
    //                }
    //                else{
    //                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), Constants.BlockFlags.DEFAULT);
    //                }
    //            }
    //        }
    //        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    //    }
    
    public BlockState getState(Level world, BlockPos pos, BlockState oldState) {
        return oldState.setValue(UP, canConnectToPipe(world, pos, Direction.UP)).setValue(DOWN, canConnectToPipe(world, pos, Direction.DOWN)).setValue(NORTH, canConnectToPipe(world, pos, Direction.NORTH)).setValue(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH)).setValue(EAST, canConnectToPipe(world, pos, Direction.EAST)).setValue(WEST, canConnectToPipe(world, pos, Direction.WEST));
    }
    
    @Override
    public boolean canConnectToPipe(BlockGetter world, BlockPos pos, Direction facing) {
        BlockEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).isPresent());
    }
    
    @Override
    public boolean canConnectToCapability(BlockGetter world, BlockPos pos, Direction facing) {
        return false;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityHighPressureFluidPipe(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide ? null : ($0, $1, $2, blockEntity) -> ((BlockEntityHighPressureFluidPipe) blockEntity).tick();
    }
}
