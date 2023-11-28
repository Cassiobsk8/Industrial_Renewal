package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.Block3x3x3Base;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityMiner;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.item.ItemDrill;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;


public class BlockMiner extends Block3x3x3Base<BlockEntityMiner> implements EntityBlock {
    public BlockMiner(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        BlockEntityMiner te = (BlockEntityMiner) world.getBlockEntity(pos);
        if (te != null) te.dropAllItems();
        super.onRemove(state, world, pos, oldState, flag);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        BlockEntityMiner tile = (BlockEntityMiner) worldIn.getBlockEntity(pos);
        IItemHandler itemHandler = tile.getDrillHandler();
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemDrill || heldItem.getItem() instanceof ItemScrewdriver))
        {
            if (heldItem.getItem() instanceof ItemDrill && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isClientSide)
                {
                    ItemStack insertedItem = itemHandler.insertItem(0, heldItem.copy(), false);
                    if(insertedItem.isEmpty())
                        if (!player.isCreative()) heldItem.shrink(1);
//                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
//                    heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemScrewdriver && !itemHandler.getStackInSlot(0).isEmpty() && !tile.isRunning())
            {
                if (!worldIn.isClientSide)
                {
                    player.addItem(itemHandler.extractItem(0, 64, false));
                }
                return InteractionResult.SUCCESS;
            }
        }
//        return InteractionResult.PASS;
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.MINER_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityMiner)blockEntity).tick();
    }
}
