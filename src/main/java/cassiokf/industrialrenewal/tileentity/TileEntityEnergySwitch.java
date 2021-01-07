package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityEnergySwitch extends TileEntityToggleableBase
{
    private static final VoltsEnergyContainer dummyEnergy = new VoltsEnergyContainer(0, 0, 0)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }
    };

    private final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
    {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return TileEntityEnergySwitch.this.passEnergyOut(maxReceive, simulate);
        }
    };

    @Override
    public void playSwitchSound()
    {
        float pitch = rand.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), IRSoundRegister.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F,
                pitch);
    }

    public int passEnergyOut(int maxReceive, boolean simulate)
    {
        if (!active) return 0;
        EnumFacing faceToFill = getOutPutFace();
        TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
        if (active && teOut != null)
        {
            IEnergyStorage storage = teOut.getCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite());
            if (storage != null) return storage.receiveEnergy(maxReceive, simulate);
        }
        return 0;
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
            }
            else if (facing == faceToFill.getOpposite())
            {
                return CapabilityEnergy.ENERGY.cast(energyContainer);
            }
        }
        return super.getCapability(capability, facing);
    }
}