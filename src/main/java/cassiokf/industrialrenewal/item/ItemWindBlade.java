package cassiokf.industrialrenewal.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWindBlade extends ItemBase
{
    private static int maxDamage = 100;//IRConfig.MainConfig.Main.ironBladeDurability;

    public ItemWindBlade(Item.Properties properties)
    {
        super(properties.maxStackSize(1).maxDamage(maxDamage));
    }

    public static ItemStack copyStack(ItemStack stack, int n)
    {
        return new ItemStack(stack.getItem(), n, stack.serializeNBT());
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        int dmg = stack.getDamage();
        if (dmg == maxDamage)
        {
            return new ItemStack(stack.getItem(), 1, stack.serializeNBT());
        }
        ItemStack tr = copyStack(stack, 1);
        //tr.attemptDamageItem(dmg + 1);
        return tr;
    }
}
