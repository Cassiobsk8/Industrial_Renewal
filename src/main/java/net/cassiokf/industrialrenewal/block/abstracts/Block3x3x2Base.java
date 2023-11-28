package net.cassiokf.industrialrenewal.block.abstracts;


import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x2MachineBase;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class Block3x3x2Base <TE extends BlockEntity3x3x2MachineBase> extends Block3x3x3Base<TE>{
    public Block3x3x2Base(Properties properties) {
        super(properties.strength(10f, 10f));
    }


    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide)
        {
            Direction facing = state.getValue(FACING);
            boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
            boolean invert = facing == Direction.NORTH || facing == Direction.WEST;
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
                for (int y = 0; y < 3; y++) {
                    for (int z = 0; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            int finalX = (isSided ? z : x);
                            int finalZ = (isSided ? x : z);
                            BlockPos currentPos = new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ));
                            if (!(x == 0 && y == 1 && z == 0)) {
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                            }
                            BlockEntity3x3x2MachineBase te = (BlockEntity3x3x2MachineBase) world.getBlockEntity(currentPos);
                            te.masterPos =  new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                        }
                    }
                }
            }
        }
//        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        if(!world.isClientSide()) {
            if (state.getValue(MASTER)) {
                List<BlockPos> blocks = Utils.getBlocksIn3x3x2Centered(pos, state.getValue(FACING));
                for (BlockPos blockPos : blocks) {
                    world.removeBlock(blockPos, false);
                }
            }

            popResource((Level) world, pos, new ItemStack(this.asItem()));
        }
//        super.destroy(world, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);

            if (blockentity instanceof BlockEntity3x3x2MachineBase) {
                TE te = (TE)blockentity;
                te.breakMultiBlocks(state);
            }
        }
        super.onRemove(state, world, pos, oldState, flag);

    }

    @Override
    public boolean isValidPosition(Level worldIn, BlockPos pos, Direction facing)
    {
        boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
        boolean invert = facing == Direction.NORTH || facing == Direction.WEST;

        for (int y = 0; y < 3; y++)
        {
            for (int z = 0; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    int finalX = (isSided ? z : x);
                    int finalZ = (isSided ? x : z);
                    BlockPos currentPos = new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ));
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.canBeReplaced())
                        return false;
                }
            }
        }
        return true;
    }
}
