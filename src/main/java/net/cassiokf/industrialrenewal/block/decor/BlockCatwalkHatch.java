package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacingWithActivating;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class BlockCatwalkHatch extends BlockAbstractHorizontalFacingWithActivating {

    protected static final VoxelShape RDOWN_AABB = Block.box(0, 0, 0, 16, 3, 16);

    protected static final VoxelShape OPEN_NORTH_AABB = Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape OPEN_SOUTH_AABB = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape OPEN_WEST_AABB = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape OPEN_EAST_AABB = Block.box(13, 0, 0, 16, 16, 16);

    public BlockCatwalkHatch(Properties properties) {
        super(properties);
    }

    public BlockCatwalkHatch()
    {
        super(metalBasicProperties.strength(1f));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return state.getValue(ACTIVE);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return level.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state);
    }

    public VoxelShape getVoxelShape(BlockState state){
        if (state.getValue(ACTIVE))
        {
            Direction direction = state.getValue(FACING);
            switch (direction)
            {
                default:
                case NORTH:
                    return OPEN_NORTH_AABB;
                case SOUTH:
                    return OPEN_SOUTH_AABB;
                case WEST:
                    return OPEN_WEST_AABB;
                case EAST:
                    return OPEN_EAST_AABB;
            }
        } else
        {
            return RDOWN_AABB;
        }
    }
}
