package cassiokf.industrialrenewal.group;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IndRWIPItemGroup extends ItemGroup
{

    public IndRWIPItemGroup()
    {
        super(References.MODID + "wip");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ItemsRegistration.STEAMLOCOMOTIVE.get());
    }

}