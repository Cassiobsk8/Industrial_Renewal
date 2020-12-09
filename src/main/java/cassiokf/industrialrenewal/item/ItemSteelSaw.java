package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ItemSteelSaw extends ItemOreDict
{
    public ItemSteelSaw(Item.Properties properties) {
        super(properties.maxDamage(64).setNoRepair().maxStackSize(1));
    }

    public static ItemStack copyStack(ItemStack stack, int n) {
        return new ItemStack(stack.getItem(), n, stack.getDamage());
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        int dmg = stack.getDamage();
        if (dmg == getMaxDamage(stack)) {
            return new ItemStack(stack.getItem(), 0, stack.getTag());
        }
        ItemStack tr = copyStack(stack, 1);
        tr.setDamage(dmg + 1);
        return tr;
    }


    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(new StringTextComponent(I18n.format("item." + References.MODID + "." + name + ".des0")));
    }
}
