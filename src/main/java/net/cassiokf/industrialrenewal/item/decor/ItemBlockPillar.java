package net.cassiokf.industrialrenewal.item.decor;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ItemBlockPillar extends BlockItem {
    public ItemBlockPillar(Block block, Properties props) {
        super(block, props);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Block block = this.getBlock();

        if (!state.is(block) || context.getPlayer().isCrouching()) {
            return context;
        }
        else{
            Direction direction = Direction.UP;
            //int i = 0;

            BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(direction);

            while(true) {
                if (!world.isClientSide && !Level.isInSpawnableBounds(blockpos$mutable)) {
//                    Player playerentity = context.getPlayer();
//                    int j = world.getMaxBuildHeight();
//                    if (playerentity instanceof ServerPlayer && blockpos$mutable.getY() >= j) {
//                        playerentity.displayClientMessage((new TextComponent("build.tooHigh " + j)).withStyle(ChatFormatting.RED), true);
//                        SChatPacket schatpacket = new SChatPacket((new TranslationTextComponent("build.tooHigh", j)).withStyle(TextFormatting.RED), ChatType.GAME_INFO, Util.NIL_UUID);
//                        ((ServerPlayerEntity)playerentity).connection.send(schatpacket);
//                    }
                    break;
                }

                state = world.getBlockState(blockpos$mutable);
                if (!state.is(this.getBlock())) {
                    if (state.canBeReplaced(context)) {
                        //Utils.debug("return context", context.getClickedPos());
                        return BlockPlaceContext.at(context, blockpos$mutable, direction);
                    }
                    break;
                }

                blockpos$mutable.move(direction);
//                if (direction.getAxis().isHorizontal()) {
//                    ++i;
//                }
            }
        }
        return super.updatePlacementContext(context);
    }
}
