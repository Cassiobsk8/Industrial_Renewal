package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockAbstractFacing;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityAlarm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockAlarm extends BlockAbstractFacing
{
    private static final VoxelShape UP_BLOCK_AABB = Block.makeCuboidShape(2, 0, 2, 14, 7, 14);
    private static final VoxelShape DOWN_BLOCK_AABB = Block.makeCuboidShape(2, 16, 2, 14, 9, 14);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(0, 2, 2, 7, 14, 14);
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(16, 2, 2, 9, 14, 14);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(2, 2, 9, 14, 14, 16);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(2, 2, 7, 14, 14, 0);


    public BlockAlarm()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state)
    {
        Direction dir = state.get(FACING);
        switch (dir)
        {
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
            case DOWN:
                return DOWN_BLOCK_AABB;
            default:
                return UP_BLOCK_AABB;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityAlarm createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityAlarm();
    }
}