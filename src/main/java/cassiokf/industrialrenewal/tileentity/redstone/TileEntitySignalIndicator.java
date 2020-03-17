package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSignalIndicator;
import cassiokf.industrialrenewal.tileentity.abstractclass.TEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class TileEntitySignalIndicator extends TEBase
{

    public TileEntitySignalIndicator()
    {
    }

    public boolean active()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        BlockPos offsetPos = this.pos.offset(state.getValue(BlockSignalIndicator.FACING));
        if (!state.getValue(BlockSignalIndicator.ONWALL) && (this.world.isBlockPowered(this.pos) || this.world.isBlockPowered(this.pos.down())))
        {
            return true;
        } else if (state.getValue(BlockSignalIndicator.ONWALL) && (this.world.isBlockPowered(offsetPos) || this.world.isBlockPowered(this.pos)))
        {
            return true;
        }
        return false;
    }
}
