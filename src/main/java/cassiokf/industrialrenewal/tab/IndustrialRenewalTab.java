package cassiokf.industrialrenewal.tab;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalTab extends CreativeTabs {

    public IndustrialRenewalTab() {
        super(IndustrialRenewal.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.ingotSteel);
    }

}