package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSignalIndicator;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class TileEntitySignalIndicator extends TEBase
{

    public TileEntitySignalIndicator(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public boolean active()
    {
        BlockState state = this.world.getBlockState(this.pos);
        BlockPos offsetPos = this.pos.offset(state.get(BlockSignalIndicator.FACING));
        if (!state.get(BlockSignalIndicator.ONWALL) && (this.world.isBlockPowered(this.pos) || this.world.isBlockPowered(this.pos.down())))
        {
            return true;
        } else return state.get(BlockSignalIndicator.ONWALL) && (this.world.isBlockPowered(offsetPos) || this.world.isBlockPowered(this.pos));
    }
}
