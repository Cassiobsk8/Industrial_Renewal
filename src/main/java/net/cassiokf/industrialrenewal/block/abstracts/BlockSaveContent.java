package net.cassiokf.industrialrenewal.block.abstracts;

import net.cassiokf.industrialrenewal.blockentity.BlockEntityBarrel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockSaveContent extends HorizontalDirectionalBlock {

    public BlockSaveContent(Properties props) {
        super(props);
    }


    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult rayTraceResult) {
        if(!world.isClientSide()){
            BlockEntity te = world.getBlockEntity(pos);
            boolean acceptFluid = FluidUtil.interactWithFluidHandler(entity, hand, world, pos, rayTraceResult.getDirection());
            if(!acceptFluid && te instanceof BlockEntityBarrel){
                BlockEntityBarrel barrel = (BlockEntityBarrel) te;
//                String message = String.format("%s: %d/%d mB", new TextComponent(barrel.getFluid()).getString(), barrel.getFluidAmount(), barrel.getMAX_CAPACITY());
//                entity.sendMessage(new TextComponent(message), entity.getUUID());
            }
//            else if(!acceptFluid && te instanceof TileEntityPortableGenerator){
//                TileEntityPortableGenerator portableGenerator = (TileEntityPortableGenerator) te;
//                String message = "";
//                if(!portableGenerator.isGenerating()){
//                    message += "NOT RUNNING: Out of fuel or no signal.\n";
//                }
//                message += String.format("%s: %d mB, %d FE/t", new TranslationTextComponent(portableGenerator.getTankContent()).getString(), portableGenerator.getTankAmount(), portableGenerator.getGenerateAmount());
//                entity.sendMessage(new StringTextComponent(message), entity.getUUID());
//            }
            else if (!acceptFluid) doAdditionalFunction(world, pos, state, entity, hand, rayTraceResult.getDirection());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        CompoundTag nbt = itemStack.getTag();
        if (nbt != null && nbt.contains("FluidName") && nbt.contains("Amount"))
        {
            //list.add(new TextComponent(nbt.getString("FluidName") + ": " + nbt.getInt("Amount")));
        }
        super.appendHoverText(itemStack, world, list, flag);
    }

    public void doAdditionalFunction(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, Direction facing)
    {
    }
}
