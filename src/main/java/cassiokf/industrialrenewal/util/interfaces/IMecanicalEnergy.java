package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IMecanicalEnergy
{
    int passRotation(int amount);

    boolean canAcceptRotation(BlockPos pos, EnumFacing side);
}
