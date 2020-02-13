package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityInfinityGenerator extends TileEntity implements ICapabilityProvider, ITickableTileEntity
{
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);

    public TileEntityInfinityGenerator()
    {
        super(TileEntityRegister.INFINITY_GENERATOR);
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }

            @Override
            public boolean canExtract()
            {
                return false;
            }
        };
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            int energy = 102400;
            for (Direction facing : Direction.Plane.HORIZONTAL)
            {
                updatePanel(facing, energy);
            }
        }
    }

    public void updatePanel(Direction facing, int energy)
    {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (facing != Direction.UP && tileEntity != null && !tileEntity.isRemoved())
        {
            final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
            if (consumer != null)
            {
                consumer.receiveEnergy(energy, false);
            }
        }
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing != Direction.DOWN && facing != Direction.UP)
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }
}
