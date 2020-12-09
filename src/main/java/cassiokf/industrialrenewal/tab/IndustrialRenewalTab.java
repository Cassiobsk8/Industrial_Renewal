package cassiokf.industrialrenewal.tab;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalTab extends CreativeTabs
{

    public IndustrialRenewalTab()
    {
        super(References.MODID);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.screwDrive);
    }

}