package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTrash extends TileFluidHandler implements ICapabilityProvider
{
    public CustomFluidTank tank = new CustomFluidTank(IRConfig.Main.barrelCapacity.get())
    {
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return resource != null ? resource.getAmount() : 0;
        }
    };
    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);

    public TileEntityTrash()
    {
        super(TileEntityRegister.TRASH);
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(10)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return true;
            }

            @Override
            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
            {
                return ItemStack.EMPTY;
            }
        };
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(1000000, 1000000, 1000000)
        {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate)
            {
                return maxReceive;
            }
        };
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> tank).cast();
        if (capability == CapabilityEnergy.ENERGY)
            return energyStorage.cast();
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }
}
