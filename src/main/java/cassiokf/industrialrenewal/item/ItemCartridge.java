package cassiokf.industrialrenewal.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCartridge extends ItemOreDict
{

    public ItemCartridge(String name, CreativeTabs tab)
    {
        super(name, "cartridgeRedstone", tab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.cartridges"));
        super.addInformation(stack, player, tooltip, advanced);
    }
}
