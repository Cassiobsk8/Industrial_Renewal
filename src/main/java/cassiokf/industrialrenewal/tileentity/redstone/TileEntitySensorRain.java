package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockSensorRain;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static cassiokf.industrialrenewal.init.TileRegistration.SENSORRAIN_TILE;

public class TileEntitySensorRain extends TileEntity implements ITickableTileEntity
{

    public TileEntitySensorRain()
    {
        super(SENSORRAIN_TILE.get());
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
