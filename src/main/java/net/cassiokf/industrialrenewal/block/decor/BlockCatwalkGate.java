package net.cassiokf.industrialrenewal.block.decor;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacingWithActivating;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCatwalkGate extends BlockAbstractHorizontalFacingWithActivating {

    protected static final VoxelShape RNORTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape RSOUTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 24, 1);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 15, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15, 0, 0, 16, 24, 16);

    public BlockCatwalkGate(Properties properties) {
        super(properties);
    }

    public BlockCatwalkGate()
    {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f));
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Vec3 hit = context.getClickLocation();
        Vec3 hitQuad = hit.subtract(Vec3.atCenterOf(context.getClickedPos()));

        return defaultBlockState().setValue(FACING, quadToDir(hitQuad));
    }

    public Direction quadToDir(Vec3 vector3d){
        if(vector3d.z > vector3d.x && vector3d.z > -vector3d.x)
            return Direction.SOUTH;
        if(vector3d.z < vector3d.x && vector3d.z < -vector3d.x)
            return Direction.NORTH;
        if(vector3d.z > vector3d.x && vector3d.z < -vector3d.x)
            return Direction.WEST;
        if(vector3d.z < vector3d.x && vector3d.z > -vector3d.x)
            return Direction.EAST;
        return Direction.NORTH;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        Direction face = state.getValue(FACING);
        if (face == Direction.NORTH)
        {
            return RNORTH_AABB;
        }
        if (face == Direction.SOUTH)
        {
            return RSOUTH_AABB;
        }
        if (face == Direction.WEST)
        {
            return RWEST_AABB;
        }
        if (face == Direction.EAST)
        {
            return REAST_AABB;
        }
        return RNORTH_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        Boolean open = state.getValue(ACTIVE);
        VoxelShape FINAL_SHAPE = NULL_SHAPE;
        Direction face = state.getValue(FACING);
        if (!open)
        {
            if (face == Direction.NORTH)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, NORTH_AABB);
            } else if (face == Direction.SOUTH)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, SOUTH_AABB);
            } else if (face == Direction.WEST)
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, WEST_AABB);
            } else
            {
                FINAL_SHAPE = Shapes.or(FINAL_SHAPE, EAST_AABB);
            }
        }
        return FINAL_SHAPE;
    }
}
