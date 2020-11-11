package cassiokf.industrialrenewal.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemDrill extends ItemBase
{
    private final int maxDamage;

    public ItemDrill(String name, CreativeTabs tab, int maxDamage)
    {
        super(name, tab);
        maxStackSize = 1;
        this.maxDamage = maxDamage;
        setMaxDamage(maxDamage);
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
