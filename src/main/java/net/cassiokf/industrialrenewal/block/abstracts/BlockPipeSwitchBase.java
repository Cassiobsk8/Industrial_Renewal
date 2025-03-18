package net.cassiokf.industrialrenewal.block.abstracts;

import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockPipeSwitchBase extends IRBaseBlock {
    
    public static BooleanProperty ON_OFF = BooleanProperty.create("on_off");
    public static DirectionProperty FACING = BlockStateProperties.FACING;
    public static IntegerProperty HANDLE_FACING = IntegerProperty.create("handle_facing", 0, 3);
    
    public BlockPipeSwitchBase(Block.Properties property) {
        super(property);
        registerDefaultState(defaultBlockState().setValue(ON_OFF, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ON_OFF, FACING, HANDLE_FACING);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        
        if (handIn == InteractionHand.MAIN_HAND && player.getMainHandItem().getItem() instanceof ItemScrewdriver) {
            if (!worldIn.isClientSide()) {
                state = state.cycle(HANDLE_FACING);
                worldIn.setBlockAndUpdate(pos, state);
            }
            return InteractionResult.SUCCESS;
        }
        if (handIn == InteractionHand.MAIN_HAND) {
            if (!worldIn.isClientSide())
                worldIn.setBlockAndUpdate(pos, state.setValue(ON_OFF, !state.getValue(ON_OFF)));
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getNearestLookingDirection()).setValue(ON_OFF, false);
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return true;
    }
    
    @Override
    public float getShadeBrightness(BlockState p_60472_, BlockGetter p_60473_, BlockPos p_60474_) {
        return 1f;
    }
}
