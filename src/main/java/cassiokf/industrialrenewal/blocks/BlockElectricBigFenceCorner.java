package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockElectricBigFenceCorner extends BlockElectricBigFenceColumn
{
    public BlockElectricBigFenceCorner()
    {
        super();
    }

    @Override
    public boolean IsBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceCorner;
    }

    @Override
    public boolean ActiveSide(IBlockReader world, BlockPos pos, BlockState state, boolean left, boolean top, boolean down)
    {
        int index = state.get(INDEX);
        if (!top && index == 2) return false;
        if (top && index != 2) return false;
        if (!down && index == 0) return false;
        if (down && index != 0) return false;
        Direction facing = state.get(FACING);
        for (final Direction face : Direction.Plane.HORIZONTAL)
        {
            if ((left && face == facing) || (!left && face == facing.rotateY()))
            {
                BlockState sideState = world.getBlockState(pos.offset(face));
                Block block = sideState.getBlock();
                return sideState.isSolid() || block instanceof BlockElectricGate || block instanceof BlockBasicElectricFence;
            }
        }
        return false;
    }
}
