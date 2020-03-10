package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSmallWindTurbine;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemWindBlade;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileEntitySmallWindTurbine extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    private final VoltsEnergyContainer energyContainer;
    public ItemStackHandler bladeInv = new ItemStackHandler(1)
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
    private float rotation;
    private int tickToDamage;

    private Random random = new Random();

    public TileEntitySmallWindTurbine()
    {
        this.energyContainer = new VoltsEnergyContainer(32000, 1024, 1024)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, bladeInv);
    }

    public static int getMaxGeneration()
    {
        return IRConfig.MainConfig.Main.maxEnergySWindTurbine;
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            int energyGen = 0;
            //Generate Energy
            if (hasBlade())
            {
                energyGen = Math.round(getMaxGeneration() * getEfficiency());
                //damage blade
                if (tickToDamage >= 1200 && energyGen > 0)
                {
                    tickToDamage = 0;
                    bladeInv.getStackInSlot(0).attemptDamageItem(1, random, null);
                    if (bladeInv.getStackInSlot(0).getItemDamage() < 0) bladeInv.setStackInSlot(0, ItemStack.EMPTY);
                }
                if (tickToDamage < 1201) tickToDamage++;
            }
            //OutPut Energy
            if (energyGen > 0)
            {
                TileEntity te = world.getTileEntity(pos.down());
                if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP))
                {
                    IEnergyStorage downE = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
                    if (downE != null && downE.canReceive())
                    {
                        downE.receiveEnergy(energyGen, false);
                    }
                }
            }
        }
    }

    public IItemHandler getBladeHandler()
    {
        return this.bladeInv;
    }

    public float getRotation(float partialTicks)
    {
        float inverted = Utils.normalize(partialTicks, 1, 0);
        rotation = rotation + (4f * inverted) * getEfficiency();
        if (rotation >= 360) rotation -= 360;
        return -(rotation);
    }

    public boolean hasBlade()
    {
        return !this.bladeInv.getStackInSlot(0).isEmpty();
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

        return weatherModifier * heightModifier;
    }

    public EnumFacing getBlockFacing()
    {
        return world.getBlockState(pos).getValue(BlockSmallWindTurbine.FACING);
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
        this.bladeInv.deserializeNBT(compound.getCompoundTag("bladeInv"));
        this.tickToDamage = compound.getInteger("damageTick");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("bladeInv", this.bladeInv.serializeNBT());
        compound.setInteger("damageTick", tickToDamage);
        return super.writeToNBT(compound);
    }
}
