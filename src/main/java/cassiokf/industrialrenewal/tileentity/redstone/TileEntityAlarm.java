package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityAlarm extends TileEntity implements ITickableTileEntity
{

    private final long PERIOD = 1000L; // Adjust to suit sound timing
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public TileEntityAlarm()
    {
        super(TileEntityRegister.ALARM);
    }


    private static boolean isPoweredWire(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() == Blocks.REDSTONE_WIRE && Blocks.REDSTONE_WIRE.getStrongPower(world.getBlockState(pos), world, pos, Direction.DOWN) > 0;
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
        boolean powered = world.isBlockPowered(this.getPos())
                || isPoweredWire(this.getWorld(), this.getPos().add(1, 0, 0))
                || isPoweredWire(this.getWorld(), this.getPos().add(-1, 0, 0))
                || isPoweredWire(this.getWorld(), this.getPos().add(0, 0, 1))
                || isPoweredWire(this.getWorld(), this.getPos().add(0, 0, -1));
        return powered;
    }

    public void playThis()
    {
        if (!world.isRemote && this.checkPowered())
        {
            world.playSound(null, pos, IRSoundRegister.TILEENTITY_ALARM, SoundCategory.BLOCKS, IRConfig.MAIN.alarmVolume.get().floatValue(), 1.0F);
        }
    }
}
