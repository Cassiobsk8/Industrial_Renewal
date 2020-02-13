package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSignalIndicator;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntitySignalIndicator extends TileEntity
{

    public TileEntitySignalIndicator()
    {
        super(TileEntityRegister.SIGNAL_INDICATOR);
    }

    public boolean active()
    {
        BlockState state = getBlockState();
        BlockPos offsetPos = this.pos.offset(state.get(BlockSignalIndicator.FACING));
        if (!state.get(BlockSignalIndicator.ONWALL) && (world.isBlockPowered(this.pos) || world.isBlockPowered(this.pos.down())))
        {
            return true;
        } else
            return state.get(BlockSignalIndicator.ONWALL) && (world.isBlockPowered(offsetPos) || world.isBlockPowered(this.pos));
    }
}
