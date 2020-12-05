package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSensorRain;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntitySensorRain extends TEBase implements ITickableTileEntity
{

    public TileEntitySensorRain(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20L == 0L)
        {
            if (this.getBlockState().getBlock() instanceof BlockSensorRain)
            {
                int value = this.world.getBlockState(this.pos).get(BlockSensorRain.POWER);
                ((BlockSensorRain) this.getBlockState().getBlock()).updatePower(this.world, this.pos, value);
            }
        }
    }
}
