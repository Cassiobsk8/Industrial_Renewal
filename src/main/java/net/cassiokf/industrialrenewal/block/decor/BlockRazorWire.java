package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRazorWire extends BlockAbstractHorizontalFacing {
    
    public static final BooleanProperty FRAME = BooleanProperty.create("frame");
    
    public BlockRazorWire() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRAME);
    }
    
    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
        float damage = 1f;
        if (entityIn instanceof LivingEntity) {
            entityIn.makeStuckInBlock(state, new Vec3(0.25D, 0.05D, 0.25D));
            entityIn.hurt(entityIn.damageSources().generic(), damage);
        }
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return NULL_SHAPE;
    }
    
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState p_196271_3_, LevelAccessor worldIn, BlockPos currentPos, BlockPos p_196271_6_) {
        Direction face = stateIn.getValue(FACING);
        if (facing == face.getClockWise())
            stateIn = stateIn.setValue(FRAME, canConnect((Level) worldIn, currentPos, stateIn));
        worldIn.setBlock(currentPos, stateIn, 3);
        return stateIn;
    }
    
    //    @Override
    //    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
    //        Direction face = state.getValue(FACING);
    //        if (facing == face.getClockWise()) return state.setValue(FRAME, canConnect(world, pos, state));
    //        world.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
    //        super.neighborChanged(state, world, pos, block, neighborPos, flag);
    //    }
    
    private boolean canConnect(Level world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        return !(world.getBlockState(pos.relative(facing.getClockWise())).getBlock() instanceof BlockRazorWire);
    }
}
