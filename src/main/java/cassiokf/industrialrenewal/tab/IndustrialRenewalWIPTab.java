package cassiokf.industrialrenewal.tab;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalWIPTab extends CreativeTabs
{

    public IndustrialRenewalWIPTab()
    {
        super(References.MODID + "wip");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.steamLocomotive);
    }

}