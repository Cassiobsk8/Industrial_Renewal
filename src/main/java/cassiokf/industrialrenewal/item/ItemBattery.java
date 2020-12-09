package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends ItemBase implements ICapabilityProvider
{
    private final CustomEnergyStorage container = new CustomEnergyStorage(
            IRConfig.Main.batteryBankCapacity.get() / 5,
            IRConfig.Main.batteryBankMaxInput.get() / 5,
            IRConfig.Main.batteryBankMaxOutput.get() / 5);

    public ItemBattery(Properties properties)
    {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored()));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
    {
        return this;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> container).cast();
        return null;
    }
}
