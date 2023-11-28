package net.cassiokf.industrialrenewal.block.abstracts;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;

public abstract class Block3x3x3Base<TE extends BlockEntity3x3x3MachineBase> extends BlockAbstractHorizontalFacing{

    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public Block3x3x3Base(Properties properties) {
        super(properties.strength(10f, 10f));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(MASTER, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos =  context.getClickedPos();
        if(isValidPosition(world, pos, context.getHorizontalDirection()))
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(MASTER, false);
        return Blocks.AIR.defaultBlockState();
    }


    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1.0f;
        //return super.getShadeBrightness(p_220080_1_, p_220080_2_, p_220080_3_);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide)
        {
            Direction facing = state.getValue(FACING);
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            //Utils.debug("checking", x, y, z, (x != 0 && y != 1 && z != 0 ));
                            if (!(x == 0 && y == 1 && z == 0)) {
                                BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                                //Utils.debug("placing", currentPos, x, y, z);
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                                BlockEntity3x3x3MachineBase te = (BlockEntity3x3x3MachineBase) world.getBlockEntity(currentPos);
                                //te.getMaster();
                                //Utils.debug("Master pos", te.getMaster().getBlockPos());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        //if (state.getBlock() == newState.getBlock()) return;
        if(!world.isClientSide())
        {
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);

            for(BlockPos blockPos : blocks){
                BlockEntity te = world.getBlockEntity(blockPos);
                if(te != null){
                    if(te instanceof BlockEntity3x3x3MachineBase && ((BlockEntity3x3x3MachineBase)te).isMaster()){
                        ((BlockEntity3x3x3MachineBase)te).breakMultiBlocks(state);
                        popResource((Level) world, te.getBlockPos(), new ItemStack(this.asItem()));
                    }
                }
            }
        }
        super.destroy(world, pos, state);
    }

    public boolean isValidPosition(Level worldIn, BlockPos pos, Direction facing)
    {
        Player player = worldIn.getNearestPlayer(TargetingConditions.forNonCombat(), pos.getX(), pos.getY(), pos.getZ());
        if (player == null) return false;
        for (int y = 0; y < 3; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    BlockPos currentPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.canBeReplaced())
                        return false;
                }
            }
        }
        //Utils.debug("valid for placement");
        return true;
    }
}
