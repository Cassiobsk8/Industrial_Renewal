package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTrash extends TEBase
{
    public static final FluidTank tank = new FluidTank(IRConfig.MainConfig.Main.barrelCapacity)
    {
        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            return resource != null ? resource.amount : 0;
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
    private static final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(1000000, 1000000, 1000000)
    {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return maxReceive;
        }
    };

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        if (capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(energyContainer);
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        return super.getCapability(capability, facing);
    }
}
