package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public interface ICompressedFluidCapability
{
    boolean canAccept(Direction face, BlockPos pos);

    boolean canPipeConnect(Direction face, BlockPos pos);

    int passCompressedFluid(int amount, int y, boolean simulate);
}
