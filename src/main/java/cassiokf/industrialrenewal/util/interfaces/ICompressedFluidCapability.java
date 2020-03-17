package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ICompressedFluidCapability
{
    boolean canAccept(EnumFacing face, BlockPos pos);

    boolean canPipeConnect(EnumFacing face, BlockPos pos);

    int passCompressedFluid(int amount, int y, boolean simulate);
}
