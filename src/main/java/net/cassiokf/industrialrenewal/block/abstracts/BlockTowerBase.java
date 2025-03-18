package net.cassiokf.industrialrenewal.block.abstracts;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BlockTowerBase<TE extends BlockEntity3x3x3MachineBase> extends Block3x3x3Base<TE> {
    
    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    
    public BlockTowerBase(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BASE, false).setValue(TOP, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BASE, TOP);
    }
    
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if (!world.isClientSide) {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            Direction facing = state.getValue(FACING);
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true).setValue(BASE, true).setValue(TOP, true));
                
                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                            if (!(x == 0 && y == 1 && z == 0)) {
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false).setValue(BASE, true).setValue(TOP, true));
                                BlockEntity3x3x3MachineBase te = (BlockEntity3x3x3MachineBase) world.getBlockEntity(currentPos);
                            }
                        }
                    }
                }
            }
        }
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }
}
