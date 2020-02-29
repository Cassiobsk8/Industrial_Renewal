package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockLight extends BlockAbstractFacing
{
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(5, 3, 0, 11, 13, 5);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(5, 3, 11, 11, 13, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(11, 3, 5, 16, 13, 11);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 3, 5, 5, 13, 11);
    protected static final VoxelShape UP_AABB = Block.makeCuboidShape(5, 11, 3, 11, 16, 13);
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(5, 0, 3, 11, 5, 13);

    public BlockLight()
    {
        super(Block.Properties.create(Material.IRON).lightValue(15));
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state)
    {
        Direction dir = state.get(FACING);
        switch (dir)
        {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case DOWN:
                return DOWN_AABB;
            default:
                return UP_AABB;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getFace().getOpposite());
    }
}
