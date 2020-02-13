package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityEnergySwitch extends TileEntityToggleableBase
{
    private final LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);

    public TileEntityEnergySwitch()
    {
        super(TileEntityRegister.ENERGY_SWITCH);
    }

    private IEnergyStorage createEnergyDummy()
    {
        return new CustomEnergyStorage(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    @Override
    public void playSwitchSound()
    {
        Random r = new Random();
        float pitch = r.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), IRSoundRegister.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F,
                pitch);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        Direction faceToFill = getOutPutFace();

        if (capability == CapabilityEnergy.ENERGY)
        {
            if (facing == faceToFill)
            {
                return dummyEnergy.cast();
            } else if (facing == faceToFill.getOpposite())
            {
                TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
                if (active && teOut != null)
                {
                    return (LazyOptional<T>) teOut.getCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite()).orElse(null);
                } else
                {
                    return dummyEnergy.cast();
                }
            }
        }
        return super.getCapability(capability, facing);
    }
}