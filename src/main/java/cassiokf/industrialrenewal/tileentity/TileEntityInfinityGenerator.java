package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityInfinityGenerator extends TEBase implements ITickableTileEntity
{
    public static final CustomEnergyStorage energyContainer = new CustomEnergyStorage(0, 0, 0)
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

    public TileEntityInfinityGenerator(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !this.world.isRemote)
        {
            for (Direction facing : Direction.Plane.HORIZONTAL)
            {
                updatePanel(facing, Integer.MAX_VALUE);
            }
        }
    }

    public void updatePanel(Direction facing, int energy)
    {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (tileEntity != null && !tileEntity.isRemoved())
        {
            final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
            if (consumer != null && consumer.canReceive())
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
            return LazyOptional.of(() -> energyContainer).cast();
        return super.getCapability(capability, facing);
    }
}
