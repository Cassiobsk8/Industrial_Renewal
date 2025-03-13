package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeBase;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityFluidPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

public class BlockFluidPipe extends BlockPipeBase<BlockEntityFluidPipe> implements EntityBlock {

    public BlockFluidPipe() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), 4, 4);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos(), defaultBlockState());
    }

    public BlockState getState(Level world, BlockPos pos, BlockState oldState){
        return oldState
                .setValue(UP, canConnectToPipe(world, pos, Direction.UP))
                .setValue(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
                .setValue(NORTH, canConnectToPipe(world, pos, Direction.NORTH))
                .setValue(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH))
                .setValue(EAST, canConnectToPipe(world, pos, Direction.EAST))
                .setValue(WEST, canConnectToPipe(world, pos, Direction.WEST))
                .setValue(CUP, canConnectToCapability(world, pos, Direction.UP))
                .setValue(CDOWN, canConnectToCapability(world, pos, Direction.DOWN))
                .setValue(CNORTH, canConnectToCapability(world, pos, Direction.NORTH))
                .setValue(CSOUTH, canConnectToCapability(world, pos, Direction.SOUTH))
                .setValue(CEAST, canConnectToCapability(world, pos, Direction.EAST))
                .setValue(CWEST, canConnectToCapability(world, pos, Direction.WEST));
    }
    
    @Override
    public boolean canConnectToPipe(BlockGetter world, BlockPos pos, Direction facing) {
        Block block = world.getBlockState(pos.relative(facing)).getBlock();
        return (block instanceof BlockFluidPipe);
    }
    
    @Override
    public boolean canConnectToCapability(BlockGetter world, BlockPos pos, Direction facing) {
        BlockEntity te = world.getBlockEntity(pos.relative(facing));
        Block block = world.getBlockState(pos.relative(facing)).getBlock();
        return (te != null
                && !(block instanceof BlockFluidPipe)
                && te.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).isPresent());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityFluidPipe(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityFluidPipe)blockEntity).tick();
    }
}
