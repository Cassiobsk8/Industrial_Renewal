package cassiokf.industrialrenewal.group;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IndRItemGroup extends ItemGroup
{

    public IndRItemGroup()
    {
        super(References.MODID);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ItemsRegistration.SCREWDRIVE.get());
    }

}