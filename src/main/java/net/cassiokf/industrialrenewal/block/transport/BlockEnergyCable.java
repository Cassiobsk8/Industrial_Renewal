package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeBase;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityEnergyCableHV;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityEnergyCableLV;
import net.cassiokf.industrialrenewal.blockentity.transport.BlockEntityEnergyCableMV;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.cassiokf.industrialrenewal.util.enums.EnumCableIn;
import net.cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<BlockEntityEnergyCable> implements EntityBlock {
    
    public EnumEnergyCableType type;
    
    public BlockEnergyCable(EnumEnergyCableType type) {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1.0f), 4, 4);
        this.type = type;
    }
    
    public static EnumCableIn convertFromType(EnumEnergyCableType type) {
        return switch (type) {
            case MV -> EnumCableIn.MV;
            case HV -> EnumCableIn.HV;
            default -> EnumCableIn.LV;
        };
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if (player.getItemInHand(handIn).getItem() == ModBlocks.ENERGY_LEVEL.get().asItem()) {
            if (!worldIn.isClientSide) {
                BlockEnergyCableMeter block = switch (type) {
                    case MV -> ModBlocks.ENERGYCABLE_MV_METER.get();
                    case HV -> ModBlocks.ENERGYCABLE_HV_METER.get();
                    default -> ModBlocks.ENERGYCABLE_LV_METER.get();
                };
                worldIn.setBlockAndUpdate(pos, block.getState(worldIn, pos, block.defaultBlockState()).setValue(BlockEnergyCableMeter.FACING, player.getDirection()));
                ItemScrewdriver.playSound(worldIn, pos);
                if (!player.isCreative()) player.getItemInHand(handIn).shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        int amount = switch (type) {
            case MV -> BlockEntityEnergyCableMV.MAX_ENERGY;
            case HV -> BlockEntityEnergyCableHV.MAX_ENERGY;
            default -> BlockEntityEnergyCableLV.MAX_ENERGY;
        };
        tooltip.add(Component.literal(amount + " FE/t"));
        super.appendHoverText(stack, world, tooltip, flag);
    }
    
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos(), defaultBlockState());
    }
    
    @Override
    public boolean canConnectToPipe(BlockGetter world, BlockPos pos, Direction facing) {
        Block block = world.getBlockState(pos.relative(facing)).getBlock();
        return (block instanceof BlockEnergyCable && type.equals(((BlockEnergyCable) block).type));
    }
    
    @Override
    public boolean canConnectToCapability(BlockGetter world, BlockPos pos, Direction facing) {
        BlockEntity te = world.getBlockEntity(pos.relative(facing));
        Block block = world.getBlockState(pos.relative(facing)).getBlock();
        return (te != null && !(block instanceof BlockEnergyCable) && te.getCapability(ForgeCapabilities.ENERGY, facing.getOpposite()).isPresent());
    }
    
    public BlockState getState(Level world, BlockPos pos, BlockState oldState) {
        return oldState.setValue(UP, canConnectToPipe(world, pos, Direction.UP)).setValue(DOWN, canConnectToPipe(world, pos, Direction.DOWN)).setValue(NORTH, canConnectToPipe(world, pos, Direction.NORTH)).setValue(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH)).setValue(EAST, canConnectToPipe(world, pos, Direction.EAST)).setValue(WEST, canConnectToPipe(world, pos, Direction.WEST)).setValue(CUP, canConnectToCapability(world, pos, Direction.UP)).setValue(CDOWN, canConnectToCapability(world, pos, Direction.DOWN)).setValue(CNORTH, canConnectToCapability(world, pos, Direction.NORTH)).setValue(CSOUTH, canConnectToCapability(world, pos, Direction.SOUTH)).setValue(CEAST, canConnectToCapability(world, pos, Direction.EAST)).setValue(CWEST, canConnectToCapability(world, pos, Direction.WEST));
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch (type) {
            case LV -> ModBlockEntity.ENERGYCABLE_LV_TILE.get().create(pos, state);
            case MV -> ModBlockEntity.ENERGYCABLE_MV_TILE.get().create(pos, state);
            case HV -> ModBlockEntity.ENERGYCABLE_HV_TILE.get().create(pos, state);
        };
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide ? null : ($0, $1, $2, blockEntity) -> ((BlockEntityEnergyCable) blockEntity).tick();
    }
}
