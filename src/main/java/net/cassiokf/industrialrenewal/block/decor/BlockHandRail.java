package net.cassiokf.industrialrenewal.block.decor;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockHandRail extends BlockAbstractHorizontalFacing {

    protected static final VoxelShape RNORTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape RSOUTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 24, 1);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 1, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15, 0, 0, 16, 24, 16);

    public BlockHandRail(Properties properties) {
        super(properties);
    }
    public BlockHandRail()
    {
        super(metalBasicProperties.strength(1f));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
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
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            if (handIn == InteractionHand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.SCREW_DRIVER.get())) {
                    state = state.cycle(FACING);
                    worldIn.setBlock(pos, state, 2);
                    return InteractionResult.SUCCESS;
                }
            }
            else
                return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state, true);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state, false);
    }

    private VoxelShape getVoxelShape(BlockState state, boolean isForRender)
    {
        Direction face = state.getValue(FACING);
        if (face == Direction.NORTH)
        {
            return isForRender ? RNORTH_AABB : NORTH_AABB;
        }
        if (face == Direction.SOUTH)
        {
            return isForRender ? RSOUTH_AABB : SOUTH_AABB;
        }
        if (face == Direction.WEST)
        {
            return isForRender ? RWEST_AABB : WEST_AABB;
        } else
        {
            return isForRender ? REAST_AABB : EAST_AABB;
        }
    }
}
