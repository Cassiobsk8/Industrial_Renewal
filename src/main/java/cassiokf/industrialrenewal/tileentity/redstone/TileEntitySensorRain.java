package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSensorRain;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySensorRain extends TileEntity implements ITickableTileEntity
{

    public TileEntitySensorRain()
    {
        super(TileEntityRegister.SENSOR_RAIN);
    }

    @Override
    public void tick()
    {
        if (world != null && !world.isRemote && world.getGameTime() % 20L == 0L)
        {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockSensorRain)
            {
                int value = state.get(BlockSensorRain.POWER);
                ((BlockSensorRain) state.getBlock()).updatePower(world, pos, value);
            }
        }
    }
}
