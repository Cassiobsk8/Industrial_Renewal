package cassiokf.industrialrenewal.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemSteelSaw extends ItemBase {

    public ItemSteelSaw(String name) {
        super(name);
        setMaxDamage(1);
        maxStackSize = 1;
        setContainerItem(this);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 64;
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        list.add("On use can remove the block");
    }

}
