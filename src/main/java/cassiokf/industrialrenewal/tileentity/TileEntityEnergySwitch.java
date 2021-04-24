package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityEnergySwitch extends TileEntityToggleableBase
{
    private static final CustomEnergyStorage dummyEnergy = new CustomEnergyStorage(0, 0, 0)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }
    };

    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
    {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return TileEntityEnergySwitch.this.passEnergyOut(maxReceive, simulate);
        }
    };

    public TileEntityEnergySwitch(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public int passEnergyOut(int maxReceive, boolean simulate)
    {
        if (!active) return 0;
        Direction faceToFill = getOutPutFace();
        TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
        if (active && teOut != null)
        {
            IEnergyStorage storage = teOut.getCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite()).orElse(null);
            if (storage != null) return storage.receiveEnergy(maxReceive, simulate);
        }
        return 0;
    }

    @Override
    public void playSwitchSound()
    {
        float pitch = rand.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), SoundsRegistration.TILEENTITY_VALVE_CHANGE.get(), SoundCategory.BLOCKS, 1F,
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
                return LazyOptional.of(() -> dummyEnergy).cast();
            } else if (facing == faceToFill.getOpposite())
            {
                return LazyOptional.of(() -> energyContainer).cast();
            }
        }
        return super.getCapability(capability, facing);
    }
}