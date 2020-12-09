package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWindBlade extends ItemBase
{
    private static final int maxDamage = IRConfig.Main.ironBladeDurability.get();

    public ItemWindBlade(Item.Properties properties)
    {
        super(properties.maxDamage(maxDamage).maxStackSize(1));
    }

    public static ItemStack copyStack(ItemStack stack, int n)
    {
        return new ItemStack(stack.getItem(), n, stack.getTag());
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        int dmg = stack.getDamage();
        if (dmg == maxDamage)
        {
            return new ItemStack(stack.getItem(), 0, stack.getTag());
        }
        ItemStack tr = copyStack(stack, 1);
        tr.setDamage(dmg + 1);
        return tr;
    }
}
