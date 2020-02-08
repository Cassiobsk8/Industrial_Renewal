package cassiokf.industrialrenewal.group;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IndustrialRenewalWIPItemGroup extends ItemGroup
{

    public IndustrialRenewalWIPItemGroup()
    {
        super(References.MODID + "wip");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.steamLocomotive);
    }

}