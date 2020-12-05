package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TEHorizontalDirection extends TEBase
{
    private Direction blockFacing;

    public TEHorizontalDirection(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public Direction getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public Direction forceFaceCheck()
    {
        BlockState state = world.getBlockState(pos);
        blockFacing = state.getBlock() instanceof BlockHorizontalFacing
                ? state.get(BlockHorizontalFacing.FACING)
                : Direction.NORTH;
        return blockFacing;
    }
}
