package net.cassiokf.industrialrenewal.block.dam;


import net.cassiokf.industrialrenewal.block.abstracts.Block3x3x3Base;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamGenerator;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockDamGenerator extends Block3x3x3Base<BlockEntityDamGenerator> implements EntityBlock {
    public static BooleanProperty NO_COLLISION = BooleanProperty.create("no_collision");
    public BlockDamGenerator(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(NO_COLLISION, false));
    }
//    public BlockDamGenerator() {
//        super(metalBasicProperties);
//        registerDefaultState(defaultBlockState().setValue(NO_COLLISION, false));
//    }


    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
        if(!world.isClientSide){
            BlockPos center = pos.above(2);
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    if(i != 0 || j != 0) {
                        BlockPos target = new BlockPos(center.getX() + i, center.getY(), center.getZ() + j);
                        world.setBlock(target, state.setValue(NO_COLLISION, true), 3);
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NO_COLLISION);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        if(state.getValue(NO_COLLISION))
            return NULL_SHAPE;
        return super.getCollisionShape(state, p_220071_2_, p_220071_3_, p_220071_4_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if(state.getValue(NO_COLLISION))
            return NULL_SHAPE;
        return super.getShape(state, p_220053_2_, p_220053_3_, p_220053_4_);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.DAM_GENERATOR.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityDamGenerator)blockEntity).tick();
    }
}
