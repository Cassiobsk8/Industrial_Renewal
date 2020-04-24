package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityEnergySwitch extends TileEntityToggleableBase
{
    private final VoltsEnergyContainer dummyEnergy;
    private final Random r = new Random();

    public TileEntityEnergySwitch()
    {
        this.dummyEnergy = new VoltsEnergyContainer(0, 0, 0)
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
        float pitch = r.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), IRSoundRegister.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F,
                pitch);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        EnumFacing faceToFill = getOutPutFace();

        if ((capability == CapabilityEnergy.ENERGY && facing == faceToFill)
                || (capability == CapabilityEnergy.ENERGY && facing == faceToFill.getOpposite()))
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        EnumFacing faceToFill = getOutPutFace();

        if (capability == CapabilityEnergy.ENERGY)
        {
            if (facing == faceToFill)
            {
                return CapabilityEnergy.ENERGY.cast(dummyEnergy);
            } else if (facing == faceToFill.getOpposite())
            {
                TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
                if (active && teOut != null && teOut.hasCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite()))
                {
                    return CapabilityEnergy.ENERGY.cast(teOut.getCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite()));
                } else
                {
                    return CapabilityEnergy.ENERGY.cast(dummyEnergy);
                }
            }
        }
        return super.getCapability(capability, facing);
    }
}