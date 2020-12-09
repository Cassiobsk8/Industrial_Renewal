package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.Random;

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
    private static final Random r = new Random();

    public TileEntityEnergySwitch(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void playSwitchSound()
    {
        float pitch = r.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), SoundsRegistration.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F,
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
                TileEntity teOut = world.getTileEntity(pos.offset(faceToFill));
                if (active && teOut != null && teOut.getCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite()).orElse(null) != null)
                {
                    return LazyOptional.of(() -> teOut.getCapability(CapabilityEnergy.ENERGY, faceToFill.getOpposite())).cast();
                } else
                {
                    return LazyOptional.of(() -> dummyEnergy).cast();
                }
            }
        }
        return super.getCapability(capability, facing);
    }
}