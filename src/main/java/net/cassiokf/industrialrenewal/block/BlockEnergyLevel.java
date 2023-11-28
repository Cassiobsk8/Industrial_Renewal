package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractFacing;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityEnergyLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockEnergyLevel extends BlockAbstractFacing implements EntityBlock
{
    public static final VoxelShape GAUGE_SHAPE = Block.box(4,1,4,12,15,12);
    
    public static final DirectionProperty GAUGE = DirectionProperty.create("gauge", Direction.Plane.HORIZONTAL);
    
    public BlockEnergyLevel() {
        super(metalBasicProperties.strength(0.8f));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GAUGE);
    }
    
    
    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        return state.setValue(GAUGE, context.getHorizontalDirection()).setValue(FACING, context.getClickedFace().getOpposite());
    }
    
    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        BlockEntity be = level.getBlockEntity(pos);
        
        if (be instanceof BlockEntityEnergyLevel beel
                && neighbor.asLong() == pos.relative(state.getValue(FACING)).asLong()) {
            beel.forceCheck();
        }
    }
    
    @Override
    public VoxelShape getVoxelShape(BlockState state) {
        return GAUGE_SHAPE;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityEnergyLevel(pPos, pState);
    }
}