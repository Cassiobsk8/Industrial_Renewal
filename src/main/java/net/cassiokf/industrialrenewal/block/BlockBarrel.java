package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockSaveContent;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityBarrel;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static net.cassiokf.industrialrenewal.block.abstracts.IRBaseBlock.metalBasicProperties;

public class BlockBarrel extends BlockSaveContent implements EntityBlock {

    public static final BooleanProperty FRAME = BooleanProperty.create("frame");
    
    public BlockBarrel() {
        super(metalBasicProperties.strength(1f).noLootTable());
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FRAME, false));
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!world.isClientSide()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof BlockEntityBarrel beb) {
                Utils.sendChatMessage(entity, beb.getChatQuantity());
            }
        }
        return super.use(state, world, pos, entity, hand, rayTraceResult);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(FRAME, context.getPlayer().isCrouching());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRAME, FACING);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityBarrel(pos, state);
    }
}
