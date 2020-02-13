package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockTrafficLight;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityTrafficLight extends TileEntity
{


    public TileEntityTrafficLight()
    {
        super(TileEntityRegister.TRAFFIC_LIGHT);
    }

    public int active()
    {
        BlockState state = getBlockState();
        BlockPos offsetPos = this.pos.offset(state.get(BlockTrafficLight.FACING));
        if (!state.get(BlockTrafficLight.ONWALL) && (world.isBlockPowered(this.pos) || world.isBlockPowered(this.pos.down())))
        {
            int power = Math.max(world.getStrongPower(this.pos), world.getStrongPower(this.pos.down()));
            if (power > 3 && power < 10)
            {
                return 1;
            } else if (power >= 10)
            {
                return 2;
            }
        } else if (state.get(BlockTrafficLight.ONWALL) && (world.isBlockPowered(offsetPos) || world.isBlockPowered(this.pos)))
        {
            int power = Math.max(world.getStrongPower(offsetPos), world.getStrongPower(this.pos));
            if (power > 3 && power < 10)
            {
                return 1;
            } else if (power >= 10)
            {
                return 2;
            }
        }
        return 0;
    }
}
