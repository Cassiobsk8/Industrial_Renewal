package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemWindBlade extends ItemBase
{
    private static int maxDamage = IRConfig.MainConfig.Main.ironBladeDurability;

    public ItemWindBlade(String name, CreativeTabs tab)
    {
        super(name, tab);
        setMaxDamage(maxDamage);
        maxStackSize = 1;
        setContainerItem(this);
    }

    public static ItemStack copyStack(ItemStack stack, int n)
    {
        return new ItemStack(stack.getItem(), n, stack.getItemDamage());
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        int dmg = stack.getItemDamage();
        if (dmg == maxDamage)
        {
            return new ItemStack(stack.getItem(), 0, maxDamage);
        }
        ItemStack tr = copyStack(stack, 1);
        tr.setItemDamage(dmg + 1);
        return tr;
    }
}
