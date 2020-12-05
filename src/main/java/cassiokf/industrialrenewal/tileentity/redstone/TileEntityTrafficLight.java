package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockTrafficLight;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class TileEntityTrafficLight extends TEBase
{

    public TileEntityTrafficLight(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public int active()
    {
        BlockState state = getBlockState();
        BlockPos offsetPos = this.pos.offset(state.get(BlockTrafficLight.FACING));
        if (!state.get(BlockTrafficLight.ONWALL) && (this.world.isBlockPowered(this.pos) || this.world.isBlockPowered(this.pos.down())))
        {
            int power = Math.max(this.world.getStrongPower(this.pos), this.world.getStrongPower(this.pos.down()));
            if (power > 3 && power < 10)
            {
                return 1;
            } else if (power >= 10) {
                return 2;
            }
        } else if (state.get(BlockTrafficLight.ONWALL) && (this.world.isBlockPowered(offsetPos) || this.world.isBlockPowered(this.pos))) {
            int power = Math.max(this.world.getStrongPower(offsetPos), this.world.getStrongPower(this.pos));
            if (power > 3 && power < 10) {
                return 1;
            } else if (power >= 10) {
                return 2;
            }
        }
        return 0;
    }
}
