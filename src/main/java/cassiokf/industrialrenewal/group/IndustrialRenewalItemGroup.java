package cassiokf.industrialrenewal.group;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalItemGroup extends ItemGroup
{

    public IndustrialRenewalItemGroup()
    {
        super(References.MODID);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.screwDrive);
    }

}