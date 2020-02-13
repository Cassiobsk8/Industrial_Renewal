package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBatteryBank;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.item.ItemWindBlade;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileEntitySmallWindTurbine extends TileEntitySyncable implements ICapabilityProvider, ITickableTileEntity
{
    public LazyOptional<IItemHandler> bladeInv = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private float rotation;
    private int energyGenerated;
    private int tickToDamage;

    public TileEntitySmallWindTurbine()
    {
        super(TileEntityRegister.SMALL_WIND_TURBINE);
    }

    public static int getMaxGeneration()
    {
        return IRConfig.Main.maxEnergySWindTurbine.get();
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return stack.getItem() instanceof ItemWindBlade;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntitySmallWindTurbine.this.Sync();
            }
        };
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(32000, 1024, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySmallWindTurbine.this.Sync();
            }
        };
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, ((IItemHandler) bladeInv));
    }

    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            //Generate Energy
            IEnergyStorage thisEnergy = (IEnergyStorage) energyStorage;
            if (hasBlade())
            {
                int energyGen = Math.round(getMaxGeneration() * getEfficiency());
                energyGenerated = thisEnergy.receiveEnergy(energyGen, false);
                if (tickToDamage >= 1200 && energyGen > 0)
                {
                    tickToDamage = 0;
                    ((IItemHandler) bladeInv).getStackInSlot(0).attemptDamageItem(1, new Random(), null);
                    if (((IItemHandler) bladeInv).getStackInSlot(0).getDamage() < 0)
                        bladeInv.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(0, ItemStack.EMPTY));
                }
                if (tickToDamage < 1201) tickToDamage++;
            } else
            {
                energyGenerated = 0;
            }
            //OutPut Energy
            if (thisEnergy.getEnergyStored() > 0)
            {
                TileEntity te = world.getTileEntity(pos.down());
                if (te != null)
                {
                    IEnergyStorage downE = te.getCapability(CapabilityEnergy.ENERGY, Direction.UP).orElse(null);
                    if (downE != null && downE.canReceive())
                    {
                        thisEnergy.extractEnergy(downE.receiveEnergy(thisEnergy.extractEnergy(1024, true), false), false);
                        this.markDirty();
                    }
                }
            }
        } else
        {
            rotation += 4.5f * getEfficiency();
            if (rotation > 360) rotation = 0;
        }
    }

    public int getEnergyGenerated()
    {
        return energyGenerated;
    }

    public IItemHandler getBladeHandler()
    {
        return ((IItemHandler) bladeInv);
    }

    public float getRotation()
    {
        return -rotation;
    }

    public boolean hasBlade()
    {
        return !((IItemHandler) bladeInv).getStackInSlot(0).isEmpty();
    }

    private float getEfficiency()
    {
        float weatherModifier;
        if (world.isThundering())
        {
            weatherModifier = 1f;
        } else if (world.isRaining())
        {
            weatherModifier = 0.9f;
        } else
        {
            weatherModifier = 0.8f;
        }

        float heightModifier;
        float posMin = -2040f;
        if (pos.getY() - 62 <= 0) heightModifier = 0;
        else heightModifier = (pos.getY() - posMin) / (255 - posMin);
        heightModifier = MathHelper.clamp(heightModifier, 0, 1);
        //System.out.println(weatherModifier + " H " + heightModifier + " " + (pos.getY()- posMin)/(255-posMin));
        return weatherModifier * heightModifier;
    }

    public Direction getBlockFacing()
    {
        return getBlockState().get(BlockBatteryBank.FACING);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        CompoundNBT invTag = compound.getCompound("inv");
        bladeInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        energyGenerated = compound.getInt("generation");
        tickToDamage = compound.getInt("damageTick");
        //this.rotation = compound.getFloat("rotation");
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
        bladeInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        compound.putInt("generation", this.energyGenerated);
        compound.putInt("damageTick", tickToDamage);
        //compound.setFloat("rotation", this.rotation);
        return super.write(compound);
    }
}
