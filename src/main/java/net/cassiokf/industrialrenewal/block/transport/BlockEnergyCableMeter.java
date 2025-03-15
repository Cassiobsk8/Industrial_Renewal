package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockEnergyCableMeter extends BlockEnergyCable {
    
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    public BlockEnergyCableMeter(EnumEnergyCableType type) {
        super(type);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //super.createBlockStateDefinition(builder);
        builder.add(FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN, FLOOR, UPFLOOR, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if (player.getItemInHand(handIn).getItem() instanceof ItemScrewdriver) {
            if (!worldIn.isClientSide) {
                BlockEnergyCable block = switch (type) {
                    case MV -> ModBlocks.ENERGYCABLE_MV.get();
                    case HV -> ModBlocks.ENERGYCABLE_HV.get();
                    default -> ModBlocks.ENERGYCABLE_LV.get();
                };
                worldIn.setBlockAndUpdate(pos, block.getState(worldIn, pos, block.defaultBlockState()));
                if (!player.isCreative())
                    player.addItem(new ItemStack(ModBlocks.ENERGY_LEVEL.get()));
                ItemScrewdriver.playSound(worldIn, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch (type) {
            case LV -> ModBlockEntity.ENERGYCABLE_LV_METER_TILE.get().create(pos, state);
            case MV -> ModBlockEntity.ENERGYCABLE_MV_METER_TILE.get().create(pos, state);
            case HV -> ModBlockEntity.ENERGYCABLE_HV_METER_TILE.get().create(pos, state);
        };
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityEnergyCable)blockEntity).tick();
    }
}
