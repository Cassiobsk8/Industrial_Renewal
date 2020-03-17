package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstractclass.TEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBunkBed extends TEBase
{

    public TileEntityBunkBed()
    {
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }
}
