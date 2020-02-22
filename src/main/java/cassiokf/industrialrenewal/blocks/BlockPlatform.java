package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockPlatform extends BlockAbstractSixWayConnections
{

    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(0, 0.0D, 0, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 16, 0, 16, 32, 0.5D);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 16, 15.5D, 16, 32, 16);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 16, 0.0D, 0.03125D, 32, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(15.5D, 16, 0, 16, 32, 16);

    public BlockPlatform()
    {
        super(Block.Properties.create(Material.IRON).variableOpacity(), 16, 16);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        BlockPos neighborPos = currentPos.offset(neighborDirection);
        BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        BlockState ub = worldIn.getBlockState(currentPos.up());
        BlockState nub = worldIn.getBlockState(neighborPos.up());
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            return nb instanceof BlockPlatform
                    || neighborState.isSolidSide(worldIn, neighborPos, neighborDirection.getOpposite())
                    || nb instanceof RailBlock
                    || (nb instanceof BlockCatwalkStair && neighborState.get(BlockCatwalkStair.FACING) == neighborDirection.getOpposite())
                    || (ub.getBlock() instanceof BlockCatwalkGate && neighborDirection == worldIn.getBlockState(currentPos.up()).get(BlockCatwalkGate.FACING))
                    || (nub.getBlock() instanceof BlockCatwalkStair && worldIn.getBlockState(neighborPos.up()).get(BlockCatwalkStair.FACING) == neighborDirection);
        }
        if (neighborDirection == Direction.DOWN)
        {
            return neighborState.isSolid()
                    || nb instanceof BlockBrace
                    || nb instanceof BlockPlatform
                    || nb instanceof BlockPillar
                    || nb instanceof BlockColumn;
        }
        return neighborState.isSolidSide(worldIn, neighborPos, neighborDirection.getOpposite())
                || nb instanceof BlockPlatform
                || nb instanceof BlockPillar;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape finalShape = BASE_AABB;
        if (!isConnected(state, UP))
        {
            if (!isConnected(state, NORTH))
            {
                finalShape = VoxelShapes.or(finalShape, NORTH_AABB);
            }
            if (!isConnected(state, SOUTH))
            {
                finalShape = VoxelShapes.or(finalShape, SOUTH_AABB);
            }
            if (!isConnected(state, WEST))
            {
                finalShape = VoxelShapes.or(finalShape, WEST_AABB);
            }
            if (!isConnected(state, EAST))
            {
                finalShape = VoxelShapes.or(finalShape, EAST_AABB);
            }
        }
        return finalShape;
    }
}
