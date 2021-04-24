package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;


public class BlockBase extends Block
{
    protected static final VoxelShape FULL_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    protected static final VoxelShape NONE_AABB = Block.makeCuboidShape(0, 0, 0, 0, 0, 0);

    public BlockBase(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        if (this.hasTileEntity(state))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TEBase) ((TEBase) te).onBlockBreak();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    public static boolean isReplaceable(IWorldReader world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        return state.getMaterial().isReplaceable();
    }

    @Override
    public final VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, worldIn, pos, true);
    }

    @Override
    public final VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.or(getVoxelShape(state, worldIn, pos, false), NONE_AABB);
    }

    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        return FULL_AABB;
    }
}