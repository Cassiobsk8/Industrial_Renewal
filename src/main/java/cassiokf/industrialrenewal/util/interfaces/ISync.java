package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISync
{
    World getThisWorld();

    BlockPos getThisPosition();

    void sync();
}
