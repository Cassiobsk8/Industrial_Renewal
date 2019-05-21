package cassiokf.industrialrenewal.tab;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.Registry.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalTab extends CreativeTabs {

    public IndustrialRenewalTab() {
        super(References.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.screwDrive);
    }

}