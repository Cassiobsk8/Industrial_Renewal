package net.cassiokf.industrialrenewal.block;


import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockTrash extends BlockAbstractHorizontalFacing implements EntityBlock {
    public BlockTrash(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    
    public BlockTrash() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2f));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection());
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.TRASH_TILE.get().create(pos, state);
    }
}
