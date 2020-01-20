package cassiokf.industrialrenewal.group;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalRailroadItemGroup extends ItemGroup
{

    public IndustrialRenewalRailroadItemGroup()
    {
        super(References.MODID + "railroad");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.barLevel);
    }

}