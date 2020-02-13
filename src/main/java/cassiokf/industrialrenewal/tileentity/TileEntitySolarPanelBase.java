package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSolarPanel;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntitySolarPanelBase extends TileEntity implements ICapabilityProvider, ITickableTileEntity
{
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);

    public TileEntitySolarPanelBase()
    {
        super(TileEntityRegister.SOLAR_PANEL_BASE);
    }

    public static int getGeneration(World world, BlockPos pos)
    {
        int i = world.getLightFor(LightType.SKY, pos) - world.getSkylightSubtracted();
        float f = world.getCelestialAngleRadians(1.0F);
        if (i > 0)
        {
            float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
            f = f + (f1 - f) * 0.2F;
            i = Math.round((float) i * MathHelper.cos(f));
        }
        i = MathHelper.clamp(i, 0, 15);
        float normalize = i / 15f;
        return Math.round(normalize * IRConfig.Main.baseSolarPanelMaxGeneration.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(600, 0, 120)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySolarPanelBase.this.markDirty();
            }
        };
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            getEnergyFromSun();
            int energy = ((IEnergyStorage) energyStorage).getEnergyStored();
            updatePanel(Direction.DOWN, energy);
        }
    }

    public void getEnergyFromSun()
    {
        IEnergyStorage thisEnergy = (IEnergyStorage) energyStorage;
        if (world.getLightManager().hasLightWork() && world.canBlockSeeSky(pos.offset(Direction.UP))
                && world.getSkylightSubtracted() == 0 && thisEnergy.getEnergyStored() != thisEnergy.getMaxEnergyStored())
        {
            int result = thisEnergy.getEnergyStored() + getGeneration(world, pos);
            if (result > thisEnergy.getMaxEnergyStored())
            {
                result = thisEnergy.getMaxEnergyStored();
            }
            final int en = result;
            energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(en));
        }
    }

    public void updatePanel(Direction facing, int energy)
    {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (facing != Direction.UP && tileEntity != null && !tileEntity.isRemoved())
        {
            if (!(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockSolarPanel))
            {
                final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
                if (consumer != null)
                {

                    ((IEnergyStorage) energyStorage).extractEnergy(consumer.receiveEnergy(energy, false), false);
                    //System.out.println("Facing " + facing + " Consumer: " + consumer.receiveEnergy(energy, true));
                }
            }
        }
    }

    @Override
    public void read(CompoundNBT compound)
    {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        return super.write(compound);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == Direction.DOWN)
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }
}
