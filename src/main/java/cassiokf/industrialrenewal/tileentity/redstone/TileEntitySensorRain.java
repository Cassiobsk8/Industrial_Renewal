package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSensorRain;
import cassiokf.industrialrenewal.tileentity.abstractclass.TEBase;
import net.minecraft.util.ITickable;

public class TileEntitySensorRain extends TEBase implements ITickable
{

    @Override
    public void update()
    {
        if (this.world != null && !this.world.isRemote && this.world.getTotalWorldTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType instanceof BlockSensorRain)
            {
                int value = this.world.getBlockState(this.pos).getValue(BlockSensorRain.POWER);
                ((BlockSensorRain) this.blockType).updatePower(this.world, this.pos, value);
            }
        }
    }
}
