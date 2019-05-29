package cassiokf.industrialrenewal.tab;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalRailroadTab extends CreativeTabs {

    public IndustrialRenewalRailroadTab() {
        super(References.MODID + "railroad");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.cargoContainer);
    }

}