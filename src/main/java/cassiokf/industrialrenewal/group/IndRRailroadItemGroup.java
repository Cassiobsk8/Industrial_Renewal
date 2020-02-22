package cassiokf.industrialrenewal.group;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IndRRailroadItemGroup extends ItemGroup
{

    public IndRRailroadItemGroup()
    {
        super(References.MODID + "railroad");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ItemsRegistration.FLUIDCONTAINER.get());
    }

}