package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractBigFence;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockElectricBigFenceWire extends BlockAbstractBigFence
{
    protected static final VoxelShape CBASE_AABB = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);

    public BlockElectricBigFenceWire()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn;
    }

    @Override
    public boolean isBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceWire;
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        if (collision) return CBASE_AABB;
        return FULL_SHAPE;
    }
}
