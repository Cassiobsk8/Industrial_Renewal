package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemSteelSaw extends ItemOreDict
{

    private static final int maxDamage = 64;

    public ItemSteelSaw(String name, String oreName, CreativeTabs tab)
    {
        super(name, oreName, tab);
        setMaxDamage(64);
        this.setNoRepair();
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


    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag)
    {
        list.add(I18n.format("item." + References.MODID + "." + name + ".des0"));
    }
}
