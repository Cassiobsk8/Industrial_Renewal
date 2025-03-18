package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractFacing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockLight extends BlockAbstractFacing {
    
    protected static final VoxelShape NORTH_AABB = Block.box(5, 3, 0, 11, 13, 5);
    protected static final VoxelShape SOUTH_AABB = Block.box(5, 3, 11, 11, 13, 16);
    protected static final VoxelShape EAST_AABB = Block.box(11, 3, 5, 16, 13, 11);
    protected static final VoxelShape WEST_AABB = Block.box(0, 3, 5, 5, 13, 11);
    protected static final VoxelShape UP_AABB = Block.box(5, 11, 3, 11, 16, 13);
    protected static final VoxelShape DOWN_AABB = Block.box(5, 0, 3, 11, 5, 13);
    
    public BlockLight() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f).lightLevel((blockState) -> 15));
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 15;
    }
    
    @Override
    public VoxelShape getVoxelShape(BlockState state) {
        Direction dir = state.getValue(FACING);
        switch (dir) {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case DOWN:
                return DOWN_AABB;
            default:
                return UP_AABB;
        }
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }
}
