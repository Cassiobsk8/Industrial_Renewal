package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockConnectedMultiblocks;
import net.cassiokf.industrialrenewal.blockentity.BlockEntitySolarPanelFrame;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class BlockSolarPanelFrame extends BlockConnectedMultiblocks<BlockEntitySolarPanelFrame> implements EntityBlock {
    
    public BlockSolarPanelFrame(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        BlockEntitySolarPanelFrame tile = (BlockEntitySolarPanelFrame) worldIn.getBlockEntity(pos);
        IItemHandler itemHandler = tile.getPanelHandler();
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && (Block.byItem(heldItem.getItem()) instanceof BlockSolarPanel || heldItem.getItem() instanceof ItemScrewdriver)) {
            if (Block.byItem(heldItem.getItem()) instanceof BlockSolarPanel && itemHandler.getStackInSlot(0).isEmpty()) {
                if (!worldIn.isClientSide) {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemScrewdriver && !itemHandler.getStackInSlot(0).isEmpty()) {
                if (!worldIn.isClientSide) {
                    ItemStack panel = itemHandler.extractItem(0, 1, false);
                    if (!player.isCreative()) player.addItem(panel);
                }
                return InteractionResult.SUCCESS;
            }
        }
        
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return false;
    }
    
    @Override
    public float getShadeBrightness(BlockState p_60472_, BlockGetter p_60473_, BlockPos p_60474_) {
        return 1.0f;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.SOLAR_PANEL_FRAME.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide ? null : ($0, $1, $2, blockEntity) -> ((BlockEntitySolarPanelFrame) blockEntity).tick();
    }
}
