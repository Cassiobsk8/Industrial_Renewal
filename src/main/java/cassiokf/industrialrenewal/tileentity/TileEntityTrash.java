package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTrash extends TEBase
{
    private static final CustomEnergyStorage energyContainer = new CustomEnergyStorage(1000000, 1000000, 1000000)
    {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return maxReceive;
        }
    };
    public static final CustomFluidTank tank = new CustomFluidTank(IRConfig.Main.barrelCapacity.get())
    {
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return resource != null ? resource.getAmount() : 0;
        }
    };
    public static final ItemStackHandler inventory = new ItemStackHandler(10)
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

    public TileEntityTrash(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> tank).cast();
        if (capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyContainer).cast();
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> inventory).cast();
        return super.getCapability(capability, facing);
    }
}
