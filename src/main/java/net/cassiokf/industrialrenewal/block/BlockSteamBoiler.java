package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.Block3x3x3Base;
import net.cassiokf.industrialrenewal.blockentity.BlockEntitySteamBoiler;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.item.ItemFireBox;
import net.cassiokf.industrialrenewal.item.ItemScrewdriver;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockSteamBoiler extends Block3x3x3Base<BlockEntitySteamBoiler> implements EntityBlock {
    
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
    
    public BlockSteamBoiler(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    //    @Override
    //    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    //
    //        //TODO: add to config
    //        int waterPtick = 100;
    //        int steamConversion = 1;
    //
    //        tooltip.add(new StringTextComponent(
    //                I18n.get("info.industrialrenewal.requires")
    //                        + ":"));
    //        tooltip.add(new StringTextComponent(" -" + I18n.get("info.industrialrenewal.firebox")));
    //        tooltip.add(new StringTextComponent(
    //                " -" + Blocks.WATER.getName().getContents()
    //                        + ": "
    //                        + waterPtick
    //                        + " mB/t"));
    //        int mult = waterPtick * steamConversion;
    //        tooltip.add(new StringTextComponent(
    //                I18n.get("info.industrialrenewal.produces")
    //                        + " "
    //                        + "Steam"
    //                        + ": "
    //                        + mult
    //                        + " mB/t"));
    //        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    //    }
    
    
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        //        BlockEntitySteamBoiler te = (BlockEntitySteamBoiler) pLevel.getBlockEntity(pPos);
        //        if (te != null && te.isMaster() && te.getIntType() == 1 && te.getFuelFill() > 0 && pRandom.nextInt(24) == 0)
        //        {
        //            worldIn.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.3F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
        //        }
        //super.animateTick(stateIn, worldIn, pos, rand);
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockEntitySteamBoiler te = (BlockEntitySteamBoiler) world.getBlockEntity(pos);
        if (te != null) te.getBoiler().dropItemsOnGround(pos);
        super.onRemove(state, world, pos, oldState, isMoving);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        BlockEntitySteamBoiler tile = (BlockEntitySteamBoiler) worldIn.getBlockEntity(pos);
        if (tile == null) return InteractionResult.FAIL;
        tile = tile.getMaster();
        ItemStack heldItem = player.getItemInHand(handIn);
        
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemFireBox || heldItem.getItem() instanceof ItemScrewdriver)) {
            if (heldItem.getItem() instanceof ItemFireBox && tile.getIntType() == 0) {
                int type = ((ItemFireBox) heldItem.getItem()).type;
                tile.setType(type);
                if (!worldIn.isClientSide && !player.isCreative()) heldItem.shrink(1);
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemScrewdriver && tile.getIntType() != 0) {
                ItemStack stack = tile.getDrop();
                if (!worldIn.isClientSide && !player.isCreative()) player.addItem(stack);
                tile.setType(0);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.STEAM_BOILER_TILE.get().create(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntitySteamBoiler) blockEntity).tick();
    }
}
