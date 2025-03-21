package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityBatteryBank;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockBatteryBank extends BlockAbstractHorizontalFacing implements EntityBlock {
    
    public static BooleanProperty NORTH_OUTPUT = BooleanProperty.create("north_out");
    public static BooleanProperty SOUTH_OUTPUT = BooleanProperty.create("south_out");
    public static BooleanProperty EAST_OUTPUT = BooleanProperty.create("east_out");
    public static BooleanProperty WEST_OUTPUT = BooleanProperty.create("west_out");
    public static BooleanProperty UP_OUTPUT = BooleanProperty.create("up_out");
    public static BooleanProperty DOWN_OUTPUT = BooleanProperty.create("down_out");
    
    public BlockBatteryBank(Properties props) {
        super(props.noOcclusion());
    }
    
    public BlockBatteryBank() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).noOcclusion().sound(SoundType.METAL).noLootTable());
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(NORTH_OUTPUT, false).setValue(SOUTH_OUTPUT, false).setValue(EAST_OUTPUT, false).setValue(WEST_OUTPUT, false).setValue(UP_OUTPUT, false).setValue(DOWN_OUTPUT, false));
    }
    
    @Override
    public Object getRenderPropertiesInternal() {
        return super.getRenderPropertiesInternal();
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH_OUTPUT, SOUTH_OUTPUT, EAST_OUTPUT, WEST_OUTPUT, UP_OUTPUT, DOWN_OUTPUT);
    }
    
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (pHand.equals(InteractionHand.MAIN_HAND) && heldItem.getItem() instanceof ItemScrewdriver) {
            BlockEntityBatteryBank tile = (BlockEntityBatteryBank) pLevel.getBlockEntity(pPos);
            if (tile != null) {
                Direction side = pHit.getDirection();
                boolean change = tile.toggleFacing(side);
                pState = pState.setValue(toggleOutput(side), change);
                pLevel.setBlockAndUpdate(pPos, pState);
                if (pLevel.isClientSide()) ItemScrewdriver.playSound(pLevel, pPos);
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    
    public BooleanProperty toggleOutput(Direction facing) {
        switch (facing) {
            case NORTH:
                return NORTH_OUTPUT;
            case SOUTH:
                return SOUTH_OUTPUT;
            case EAST:
                return EAST_OUTPUT;
            case WEST:
                return WEST_OUTPUT;
            case UP:
                return UP_OUTPUT;
            case DOWN:
                return DOWN_OUTPUT;
        }
        return NORTH_OUTPUT;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.BATTERY_BANK.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : ($0, $1, $2, blockEntity) -> ((BlockEntityBatteryBank) blockEntity).tick();
    }
}
