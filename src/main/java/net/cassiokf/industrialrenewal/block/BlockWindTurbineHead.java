package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbineHead;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.cassiokf.industrialrenewal.item.ItemWindBlade;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockWindTurbineHead extends BlockAbstractHorizontalFacing implements EntityBlock {
    public BlockWindTurbineHead(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public BlockWindTurbineHead() {
        super(metalBasicProperties.strength(0.8f).noOcclusion());
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        BlockEntityWindTurbineHead tile = (BlockEntityWindTurbineHead) worldIn.getBlockEntity(pos);
        if (tile == null) return InteractionResult.PASS;

        IItemHandler itemHandler = tile.getBladeHandler();
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemWindBlade || heldItem.getItem() instanceof ItemScrewdriver))
        {
            if (heldItem.getItem() instanceof ItemWindBlade && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isClientSide())
                {
                    ItemStack insertedItem = itemHandler.insertItem(0, heldItem.copy(), false);
                    if(insertedItem.isEmpty())
                        if (!player.isCreative()) heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemScrewdriver && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isClientSide())
                {
                    player.addItem(itemHandler.extractItem(0, 64, false));
                }
                return InteractionResult.SUCCESS;
            }
        }
//        return ActionResultType.PASS;
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @org.jetbrains.annotations.Nullable BlockGetter p_49817_, List<Component> tooltip, TooltipFlag p_49819_) {
//        tooltip.add(new TextComponent(String.format("Produces %d FR/t max", 128)));
        super.appendHoverText(p_49816_, p_49817_, tooltip, p_49819_);
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
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getPlayer().isCrouching() ? context.getHorizontalDirection().getOpposite() : context.getHorizontalDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.WIND_TURBINE_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityWindTurbineHead)blockEntity).tick();
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BlockEntityWindTurbineHead) {
                ((BlockEntityWindTurbineHead)blockentity).dropContents();
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, flag);
        }
    }
}
