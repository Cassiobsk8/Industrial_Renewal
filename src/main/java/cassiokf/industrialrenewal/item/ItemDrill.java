package cassiokf.industrialrenewal.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDrill extends ItemBase
{
    public ItemDrill(Item.Properties properties, int maxDamage)
    {
        super(properties.maxStackSize(1).maxDamage(maxDamage));
    }

    @Override
    public boolean isRepairable(ItemStack stack)
    {
        return false;
    }

    public static ItemStack copyStack(ItemStack stack, int n)
    {
        return new ItemStack(stack.getItem(), n, stack.getTag());
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        int dmg = stack.getDamage();
        if (dmg == getMaxDamage(stack))
        {
            return new ItemStack(stack.getItem(), 0, stack.getTag());
        }
        ItemStack tr = copyStack(stack, 1);
        tr.setDamage(dmg + 1);
        return tr;
    }
}
