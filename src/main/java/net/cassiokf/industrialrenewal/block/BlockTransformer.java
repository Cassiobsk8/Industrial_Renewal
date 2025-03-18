package net.cassiokf.industrialrenewal.block;


import net.cassiokf.industrialrenewal.block.abstracts.Block3x2x3Base;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityTransformer;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class BlockTransformer extends Block3x2x3Base<BlockEntityTransformer> implements EntityBlock {
    
    public static final IntegerProperty OUTPUT = IntegerProperty.create("output", 1, 2);
    public static final BooleanProperty REDSTONE = BooleanProperty.create("redstone");
    
    
    public BlockTransformer(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(OUTPUT, 1).setValue(REDSTONE, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OUTPUT, REDSTONE);
    }
    
    @Override
    public void placeAdditional(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        Direction facing = state.getValue(FACING);
        BlockPos target = pos.relative(facing.getOpposite()).relative(facing.getClockWise());
        BlockState blockState = world.getBlockState(target);
        world.setBlockAndUpdate(target, blockState.setValue(REDSTONE, true));
    }
    
    public void neighborChanged(BlockState state, Level world, BlockPos pos1, Block block, BlockPos pos2, boolean flag) {
        if (state.getValue(REDSTONE)) {
            BlockEntity te = world.getBlockEntity(pos1);
            if (te instanceof BlockEntityTransformer transformerTile) {
                if (transformerTile.masterPos != null && !world.getBlockState(transformerTile.masterPos).isAir()) {
                    BlockState masterState = world.getBlockState(transformerTile.masterPos);
                    world.setBlockAndUpdate(transformerTile.masterPos, masterState.setValue(OUTPUT, getNeighborSignal(world, pos1) ? 2 : 1));
                }
            }
        }
        super.neighborChanged(state, world, pos1, block, pos2, flag);
    }
    
    private boolean getNeighborSignal(Level world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (world.hasSignal(pos.relative(direction), direction)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.TRANSFORMER_TILE.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityTransformer) blockEntity).tick();
    }
}
