package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockElectricBigFenceColumn;
import cassiokf.industrialrenewal.blocks.BlockElectricGate;
import cassiokf.industrialrenewal.blocks.abstracts.BlockBasicElectricFence;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBigFenceBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

import static cassiokf.industrialrenewal.init.TileRegistration.BIGFENCE_CORNER;

public class TEBigFenceCorner extends TEBigFenceBase
{

    public TEBigFenceCorner()
    {
        super(BIGFENCE_CORNER.get());
    }

    @Override
    public boolean activeSide(boolean left, boolean top, boolean down)
    {
        int index = getBlockState().get(BlockElectricBigFenceColumn.INDEX);
        if (!top && index == 2) return false;
        if (top && index != 2) return false;
        if (!down && index == 0) return false;
        if (down && index != 0) return false;
        Direction facing = getBlockState().get(BlockElectricBigFenceColumn.FACING);
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
