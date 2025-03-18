package net.cassiokf.industrialrenewal.block.decor;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractSixWayConnections;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockPlatform extends BlockAbstractSixWayConnections {
    
    protected static final VoxelShape BASE_AABB = Block.box(0, 0.0D, 0, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 16, 0, 16, 32, 0.5D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 16, 15.5D, 16, 32, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 16, 0.0D, 0.03125D, 32, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15.5D, 16, 0, 16, 32, 16);
    
    public BlockPlatform() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().strength(1f), 16, 16);
    }
    
    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext context) {
        if (!context.getPlayer().isCrouching()) return context.getItemInHand().getItem() == this.asItem();
        return super.canBeReplaced(blockState, context);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.values()) {
            state = state.setValue(getPropertyBasedOnDirection(direction), canConnectTo(context.getLevel(), context.getClickedPos(), direction));
        }
        return state;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            if (handIn == InteractionHand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.SCREW_DRIVER.get())) {
                    Vec3 hitQuad = hit.getLocation().subtract(Vec3.atCenterOf(pos));
                    if (hit.getDirection() == Direction.UP) state = state.cycle(quadToDir(hitQuad));
                    else state = state.cycle(getBooleanProperty(hit.getDirection()));
                    
                    worldIn.setBlock(pos, state, 2);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
    
    public BooleanProperty getBooleanProperty(Direction face) {
        switch (face) {
            default:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case EAST:
                return EAST;
            case WEST:
                return WEST;
            case UP:
                return UP;
            case DOWN:
                return DOWN;
        }
    }
    
    public BooleanProperty quadToDir(Vec3 vector3d) {
        if (vector3d.z > vector3d.x && vector3d.z > -vector3d.x) return SOUTH;
        if (vector3d.z < vector3d.x && vector3d.z < -vector3d.x) return NORTH;
        if (vector3d.z > vector3d.x && vector3d.z < -vector3d.x) return WEST;
        if (vector3d.z < vector3d.x && vector3d.z > -vector3d.x) return EAST;
        return NORTH;
    }
    
    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        BlockPos neighborPos = currentPos.relative(neighborDirection);
        BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        BlockState ub = worldIn.getBlockState(currentPos.above());
        BlockState nub = worldIn.getBlockState(neighborPos.above());
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN) {
            return nb instanceof BlockPlatform || neighborState.isFaceSturdy(worldIn, neighborPos, neighborDirection.getOpposite()) || nb instanceof BaseRailBlock || (nb instanceof BlockCatwalkStair && neighborState.getValue(BlockCatwalkStair.FACING) == neighborDirection.getOpposite()) || (ub.getBlock() instanceof BlockCatwalkGate && neighborDirection == worldIn.getBlockState(currentPos.above()).getValue(BlockCatwalkGate.FACING)) || (nub.getBlock() instanceof BlockCatwalkStair && worldIn.getBlockState(neighborPos.above()).getValue(BlockCatwalkStair.FACING) == neighborDirection);
        }
        if (neighborDirection == Direction.DOWN) {
            return Block.canSupportRigidBlock(worldIn, neighborPos)//neighborState.isSolid()
                    //|| nb instanceof BlockBrace
                    || nb instanceof BlockPlatform || nb instanceof BlockPillar || nb instanceof BlockColumn;
        }
        return neighborState.isFaceSturdy(worldIn, neighborPos, neighborDirection.getOpposite()) || nb instanceof BlockPlatform || nb instanceof BlockPillar;
    }
    
    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter world, BlockPos pos, Entity collidingEntity) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        VoxelShape finalShape = BASE_AABB;
        if (!isConnected(state, UP)) {
            if (!isConnected(state, NORTH)) {
                finalShape = Shapes.or(finalShape, NORTH_AABB);
            }
            if (!isConnected(state, SOUTH)) {
                finalShape = Shapes.or(finalShape, SOUTH_AABB);
            }
            if (!isConnected(state, WEST)) {
                finalShape = Shapes.or(finalShape, WEST_AABB);
            }
            if (!isConnected(state, EAST)) {
                finalShape = Shapes.or(finalShape, EAST_AABB);
            }
        }
        return finalShape;
    }
    
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        for (Direction face : Direction.values()) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, 2);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }
}
