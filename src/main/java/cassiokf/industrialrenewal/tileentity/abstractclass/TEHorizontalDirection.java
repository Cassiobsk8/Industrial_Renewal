package cassiokf.industrialrenewal.tileentity.abstractclass;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public abstract class TEHorizontalDirection extends TEBase
{
    private EnumFacing blockFacing;

    public EnumFacing getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public EnumFacing forceFaceCheck()
    {
        IBlockState state = world.getBlockState(pos);
        blockFacing = state.getBlock() instanceof BlockHorizontalFacing
                ? state.getValue(BlockHorizontalFacing.FACING)
                : EnumFacing.NORTH;
        return blockFacing;
    }
}
