package cassiokf.industrialrenewal.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCartridge extends ItemOreDict
{
    public ItemCartridge(Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<ITextComponent> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.cartridges")));
        super.addInformation(stack, player, tooltip, advanced);
    }
}
