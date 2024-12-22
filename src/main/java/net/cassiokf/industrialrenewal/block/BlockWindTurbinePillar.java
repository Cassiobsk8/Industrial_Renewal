package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockConnectedMultiblocks;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbinePillar;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockWindTurbinePillar extends BlockConnectedMultiblocks<BlockEntityWindTurbinePillar> implements EntityBlock {
    public static final BooleanProperty BASE = BooleanProperty.create("base");

    public BlockWindTurbinePillar(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(BASE, false));
    }

    public BlockWindTurbinePillar() {
        super(metalBasicProperties.strength(0.8f).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(BASE, false));
    }
    
//    @Override
//    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
//        if(!context.getPlayer().isCrouching())
//            return context.getItemInHand().getItem() == this.asItem();
//        return super.canBeReplaced(state, context);
//    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        Item playerItem = player.getMainHandItem().getItem();
        Block clickedBlock = state.getBlock();
        BlockEntityWindTurbinePillar te = (BlockEntityWindTurbinePillar) worldIn.getBlockEntity(pos);
        if (te != null) {
            te.getIsBase();
            worldIn.setBlockAndUpdate(pos, state);
            return InteractionResult.SUCCESS;
        }
        //industrialrenewal.LOGGER.info("TRYING TO PLACE PILLAR ON TOP " + playerItem + Item.byBlock(ModBlocks.TURBINE_PILLAR.get()));
//        if (playerItem.equals(Item.byBlock(ModBlocks.TURBINE_PILLAR.get())) && clickedBlock.equals(ModBlocks.TURBINE_PILLAR.get()))
//        {
//            //industrialrenewal.LOGGER.info("READING PILLAR HEIGHT");
//            int n = 1;
//            while (worldIn.getBlockState(pos.above(n)).getBlock() instanceof BlockWindTurbinePillar)
//            {
//                n++;
//            }
//
//            if (worldIn.getBlockState(pos.above(n)).canBeReplaced())
//            {
//                //industrialrenewal.LOGGER.info("PLACED");
//                BlockState aboveState = state.setValue(FACING, state.getValue(FACING)).setValue(BASE, false);
//                if (!worldIn.isClientSide()) worldIn.setBlockAndUpdate(pos.above(n), aboveState);
//                if (!player.isCreative())
//                {
//                    player.getMainHandItem().shrink(1);
//                    //player.inventory.clearMatchingItems(playerItem, 0, 1, null);
//                }
//                return InteractionResult.SUCCESS;
//            }
//            //industrialrenewal.LOGGER.info("PLACEMENT FAILED");
//        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BASE);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        boolean value = !level.getBlockState(pos.below()).getBlock().equals(ModBlocks.TURBINE_PILLAR.get());
        //Utils.debug("Block Below", value);

        return super.getStateForPlacement(context).setValue(BASE, value);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
//        if(!worldIn.isClientSide() && fromPos.equals(pos.below())) {
//            boolean value = !(worldIn.getBlockEntity(pos.below()) instanceof BlockEntityWindTurbinePillar);
//
//            state = state.setValue(BASE, value);
//            worldIn.setBlockAndUpdate(pos, state);
//        }
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.TURBINE_PILLAR_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityWindTurbinePillar)blockEntity).tick();
    }
}
