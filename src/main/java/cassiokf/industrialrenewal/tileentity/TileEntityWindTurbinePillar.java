package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSmallWindTurbine;
import cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityWindTurbinePillar extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    private final VoltsEnergyContainer energyContainer;

    private int energyGenerated;

    private int tick;

    public TileEntityWindTurbinePillar()
    {
        this.energyContainer = new VoltsEnergyContainer(1024, 1024, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityWindTurbinePillar.this.Sync();
            }
        };
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            if (isBase())
            {
                for (EnumFacing face : EnumFacing.VALUES)
                {
                    TileEntity te = world.getTileEntity(pos.offset(face));
                    if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite()))
                    {
                        IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
                        if (eStorage != null && eStorage.canReceive())
                        {
                            this.energyContainer.extractEnergy(eStorage.receiveEnergy(this.energyContainer.extractEnergy(this.energyContainer.getMaxOutput(), true), false), false);
                        }
                    }
                }
            } else
            {
                if (world.getBlockState(pos.down()).getBlock() instanceof BlockWindTurbinePillar && this.energyContainer.getEnergyStored() > 0)
                {
                    TileEntity te = world.getTileEntity(pos.down());
                    assert te != null;
                    IEnergyStorage downE = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
                    if (downE != null && downE.canReceive())
                    {
                        this.energyContainer.extractEnergy(downE.receiveEnergy(this.energyContainer.extractEnergy(this.energyContainer.getMaxOutput(), true), false), false);
                    }
                }
            }
        } else if (isBase())
        {
            tick++;
            if (tick % 10 == 0)
            {
                tick = 0;
                Integer n = 1;
                while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockWindTurbinePillar)
                {
                    n++;
                }
                if (world.getBlockState(pos.up(n)).getBlock() instanceof BlockSmallWindTurbine && world.getTileEntity(pos.up(n)) instanceof TileEntityWindTurbinePillar)
                {
                    TileEntitySmallWindTurbine te = (TileEntitySmallWindTurbine) world.getTileEntity(pos.up(n));
                    if (te != null) energyGenerated = te.getEnergyGenerated();
                    else energyGenerated = 0;
                } else energyGenerated = 0;
            }
        }
    }

    public EnumFacing getBlockFacing()
    {
        return this.world.getBlockState(this.pos).getValue(BlockWindTurbinePillar.FACING);
    }

    public float getGenerationforGauge()
    {
        float currentAmount = getEnergyGenerated();
        float totalCapacity = TileEntitySmallWindTurbine.getMaxGeneration();
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public int getEnergyGenerated()
    {
        return energyGenerated;
    }

    public boolean isBase()
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockWindTurbinePillar && state.getActualState(world, pos).getValue(BlockWindTurbinePillar.DOWN);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(this.energyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }
}
