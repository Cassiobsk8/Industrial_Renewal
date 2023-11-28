package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractSixWayConnections;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockIndustrialFloor extends BlockAbstractSixWayConnections {

    protected static final VoxelShape NONE_AABB = Block.box(6, 6, 6, 10, 10, 10);

    protected static final VoxelShape C_UP_AABB = Block.box(0, 15, 0, 16, 16, 16);
    protected static final VoxelShape C_DOWN_AABB = Block.box(0, 0, 0, 16, 1, 16);
    protected static final VoxelShape C_NORTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape C_SOUTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape C_WEST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape C_EAST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    public static final BooleanProperty LIGHT = BooleanProperty.create("light");
    public BlockIndustrialFloor() {
        super(metalBasicProperties.strength(1f), 16, 16);
    }

    @Override
    protected BlockState getInitDefaultState() {
        return super.getInitDefaultState().setValue(LIGHT, false);
    }

    private static boolean isValidConnection(final BlockState neighborState, final BlockGetter world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        return nb instanceof BlockIndustrialFloor
                || (nb instanceof DoorBlock && neighborState.getValue(DoorBlock.FACING).equals(neighborDirection))
                || (neighborDirection.equals(Direction.DOWN) && nb instanceof BlockCatwalkLadder)
                || (neighborDirection.equals(Direction.UP) && nb instanceof BlockCatwalkHatch)
                //start check for horizontal Iladder
                || ((neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
                && nb instanceof BlockCatwalkLadder && !neighborState.getValue(BlockCatwalkLadder.ACTIVE))
                //end
                ;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIGHT);
    }

    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction))
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(getPropertyBasedOnDirection(direction), true), 3);
                else
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(getPropertyBasedOnDirection(direction), false), 3);
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack playerStack = player.getItemInHand(handIn);

        if(!worldIn.isClientSide){
//            if(Block.byItem(playerStack.getItem()) instanceof BlockPipeBase<?>){
//                worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
//                worldIn.setBlock(pos, Block.byItem(playerStack.getItem()).defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
//                if (!player.isCreative())
//                {
//                    playerStack.shrink(1);
//                }
//                return InteractionResult.SUCCESS;
//            }
            if(!state.getValue(LIGHT)){
                if(Block.byItem(playerStack.getItem()) instanceof BlockLight){
//                    worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    worldIn.setBlock(pos, state.setValue(LIGHT, true), 3);
                    if (!player.isCreative())
                    {
                        playerStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }

            if (handIn == InteractionHand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.SCREW_DRIVER.get())) {
                    worldIn.setBlock(pos, Block.byItem(playerStack.getItem()).defaultBlockState().setValue(LIGHT, false), 3);
                    Block.popResource(worldIn, pos, new ItemStack(ModBlocks.LIGHT.get().asItem(), 1));
                    worldIn.setBlock(pos, state, 2);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (isConnected(state, LIGHT)) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
//        if(!Config.INDUSTRIAL_FLOOR_COLLISION.get()){
//            return super.getCollisionShape(state, worldIn, pos, context);
//        }
        VoxelShape SHAPE = NULL_SHAPE;
        for(Direction face: Direction.values()){
            BooleanProperty prop = getPropertyBasedOnDirection(face);
            if(state.getValue(prop)){
                SHAPE = switch (face) {
                    case UP -> Shapes.or(SHAPE, C_UP_AABB);
                    case DOWN -> Shapes.or(SHAPE, C_DOWN_AABB);
                    case NORTH -> Shapes.or(SHAPE, C_NORTH_AABB);
                    case SOUTH -> Shapes.or(SHAPE, C_SOUTH_AABB);
                    case EAST -> Shapes.or(SHAPE, C_EAST_AABB);
                    case WEST -> Shapes.or(SHAPE, C_WEST_AABB);
                };
            }
        }
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            if(state.getValue(LIGHT))
                Block.popResource(world, pos, new ItemStack(ModBlocks.LIGHT.get().asItem(), 1));
        }
        super.onRemove(state, world, pos, oldState, flag);
    }
}
