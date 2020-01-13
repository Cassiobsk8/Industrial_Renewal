package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityInfinityGenerator extends TileEntity implements ICapabilityProvider, ITickable
{
    public final VoltsEnergyContainer energyContainer;

    public TileEntityInfinityGenerator()
    {
        this.energyContainer = new VoltsEnergyContainer(0, 0, 0)
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
    public void update()
    {
        if (this.hasWorld() && !this.world.isRemote)
        {
            int energy = 102400;
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
            {
                updatePanel(facing, energy);
            }
        }
    }

    public void updatePanel(EnumFacing facing, int energy)
    {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (facing != EnumFacing.UP && tileEntity != null && !tileEntity.isInvalid())
        {
            if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
            {
                final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                if (consumer != null)
                {
                    consumer.receiveEnergy(energy, false);
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing != EnumFacing.DOWN && facing != EnumFacing.UP) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing != EnumFacing.DOWN && facing != EnumFacing.UP)
            return CapabilityEnergy.ENERGY.cast(this.energyContainer);
        return super.getCapability(capability, facing);
    }
}
