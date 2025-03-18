package net.cassiokf.industrialrenewal.block;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractFacing;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityHVIsolator;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockHVIsolator extends BlockAbstractFacing implements EntityBlock {
    
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    
    protected static final VoxelShape NORTH_AABB = Block.box(5, 5, 0, 11, 11, 8);
    protected static final VoxelShape SOUTH_AABB = Block.box(5, 5, 8, 11, 11, 16);
    protected static final VoxelShape EAST_AABB = Block.box(8, 5, 5, 16, 11, 11);
    protected static final VoxelShape WEST_AABB = Block.box(0, 5, 5, 8, 11, 11);
    protected static final VoxelShape UP_AABB = Block.box(5, 8, 5, 11, 16, 11);
    protected static final VoxelShape DOWN_AABB = Block.box(5, 0, 5, 11, 8, 11);
    
    public BlockHVIsolator(Properties properties) {
        super(properties);
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
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        //        if(!worldIn.isClientSide){
        //            if(handIn == InteractionHand.MAIN_HAND){
        //                BlockEntityHVIsolator tileEntityWireIsolator = (BlockEntityHVIsolator)worldIn.getBlockEntity(pos);
        //                for(BlockPos node : tileEntityWireIsolator.neighbors){
        //                    Utils.debug("Node", pos, node);
        //                }
        //                Utils.debug("ALL");
        //                for(BlockPos node : tileEntityWireIsolator.allNodes){
        //                    Utils.debug("Node", pos, node);
        //                }
        //            }
        //        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace().getOpposite());
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.ISOLATOR_TILE.get().create(pos, state);
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BlockEntityHVIsolator) {
                ((BlockEntityHVIsolator) blockentity).remove();
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, oldState, flag);
        }
    }
}
