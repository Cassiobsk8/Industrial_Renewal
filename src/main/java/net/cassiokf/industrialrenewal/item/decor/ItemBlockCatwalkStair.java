package net.cassiokf.industrialrenewal.item.decor;


import net.cassiokf.industrialrenewal.block.decor.BlockCatwalk;
import net.cassiokf.industrialrenewal.block.decor.BlockCatwalkStair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ItemBlockCatwalkStair extends BlockItem {
    public ItemBlockCatwalkStair(Block block, Properties props) {
        super(block, props);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);

        if(context.getPlayer().isCrouching()){
            return context;
        }
        else if(state.getBlock() instanceof BlockCatwalk){
            BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
            if(state.canBeReplaced(context))
                return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            else
                return null;
        }
        else if (state.getBlock() instanceof BlockCatwalkStair){
            BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection()).move(Direction.UP);
            if(state.canBeReplaced(context))
                return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
        }
        return super.updatePlacementContext(context);
    }
}
