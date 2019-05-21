package cassiokf.industrialrenewal.tab;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.Registry.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalWIPTab extends CreativeTabs {

    public IndustrialRenewalWIPTab() {
        super(References.MODID + "wip");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.steamLocomotive);
    }

}