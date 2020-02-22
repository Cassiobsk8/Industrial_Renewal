package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cassiokf.industrialrenewal.init.TileRegistration.ALARM_TILE;

public class TileEntityAlarm extends TileEntity implements ITickableTileEntity
{

    private final long PERIOD = 1000L; // Adjust to suit sound timing
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public TileEntityAlarm()
    {
        super(ALARM_TILE.get());
    }


    private static boolean isPoweredWire(World world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == Blocks.REDSTONE_WIRE && state.getStrongPower(world, pos, Direction.DOWN) > 0;
    }

    @Override
    public void tick()
    {
        long thisTime = System.currentTimeMillis();
        if ((thisTime - lastTime) >= PERIOD)
        {
            lastTime = thisTime;
            playThis();
        }
    }

    public boolean checkPowered()
    {
        return world.isBlockPowered(pos)
                || isPoweredWire(world, pos.north())
                || isPoweredWire(world, pos.south())
                || isPoweredWire(world, pos.east())
                || isPoweredWire(world, pos.west());
    }

    public void playThis()
    {
        if (!world.isRemote && this.checkPowered())
        {
            world.playSound(null, pos, SoundsRegistration.TILEENTITY_ALARM.get(), SoundCategory.BLOCKS, IRConfig.MAIN.alarmVolume.get().floatValue(), 1.0F);
        }
    }
}
