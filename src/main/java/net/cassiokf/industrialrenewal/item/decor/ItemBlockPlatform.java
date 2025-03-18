package net.cassiokf.industrialrenewal.item.decor;


import net.cassiokf.industrialrenewal.block.decor.BlockPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemBlockPlatform extends BlockItem {
    public ItemBlockPlatform(Block block, Properties props) {
        super(block, props);
    }
    
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        
        if (context.getPlayer().isCrouching()) {
            return context;
        } else if (state.getBlock() instanceof BlockPlatform) {
            if (context.getClickedFace() == Direction.UP) {
                BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
                if (state.canBeReplaced(context))
                    return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            }
        }
        return super.updatePlacementContext(context);
    }
}
