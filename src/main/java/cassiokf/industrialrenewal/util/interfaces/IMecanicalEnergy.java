package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public interface IMecanicalEnergy
{
    int passRotation(int amount);

    boolean canAcceptRotation(BlockPos pos, Direction side);
}
