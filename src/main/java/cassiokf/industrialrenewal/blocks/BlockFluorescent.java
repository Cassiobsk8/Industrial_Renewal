package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockFluorescent extends BlockAbstractSixWayConnections
{
    public BlockFluorescent()
    {
        super(Block.Properties.create(Material.IRON).lightValue(15), 8, 6);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        Block nb = neighborState.getBlock();
        if (neighborDirection != Direction.UP)
        {
            return nb instanceof BlockFluorescent;
        }
        return !(nb instanceof BlockRoof) || (currentPos.getZ() % 2) == 0;
    }
}
