package net.cassiokf.industrialrenewal.item;

import net.cassiokf.industrialrenewal.blockentity.BlockEntityHVIsolator;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemWireCoil extends IRBaseItem {
    BlockEntityHVIsolator firstClickedOn = null;
    
    public ItemWireCoil(Properties props) {
        super(props);
    }
    
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        
        if (!level.isClientSide && context.getHand() == InteractionHand.MAIN_HAND && context.getItemInHand().getItem().equals(this)) {
            BlockEntity te = level.getBlockEntity(blockPos);
            if (te instanceof BlockEntityHVIsolator && firstClickedOn == null) {
                firstClickedOn = (BlockEntityHVIsolator) te;
                Utils.sendChatMessage(player, "Starting Link " + blockPos);
                return InteractionResult.PASS;
            } else if (te instanceof BlockEntityHVIsolator tempClick) {
                if (firstClickedOn == tempClick) {
                    Utils.sendChatMessage(player, "Link Cancelled");
                } else if (Utils.distance(firstClickedOn.getBlockPos(), blockPos) > 48) {
                    Utils.sendChatMessage(player, "Link Cancelled, Too far");
                } else {
                    if (firstClickedOn.link(tempClick)) {
                        Utils.sendChatMessage(player, "Link Complete " + blockPos);
                        context.getItemInHand().shrink(1);
                    } else {
                        Utils.sendChatMessage(player, "Already Linked");
                    }
                }
                firstClickedOn = null;
                return InteractionResult.PASS;
            }
        }
        return super.useOn(context);
    }
}
