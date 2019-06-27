package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends ItemBase implements ICapabilityProvider
{

    private final VoltsEnergyContainer container;

    public ItemBattery(String name, CreativeTabs tab)
    {
        super(name, tab);
        this.container = new VoltsEnergyContainer(IRConfig.MainConfig.Main.batteryBankCapacity / 5, IRConfig.MainConfig.Main.batteryBankMaxInput / 5, IRConfig.MainConfig.Main.batteryBankMaxOutput / 5);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag)
    {
        list.add("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(this.container);
        return null;
    }
}
