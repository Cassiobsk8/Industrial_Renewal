package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.world.generation.OreGeneration;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemRegenerationWand extends ItemBase
{
    public ItemRegenerationWand(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("info.industrialrenewal.prospectingpan.info") + " (Creative only)");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (!worldIn.isRemote)
        {
            ItemStack stack = OreGeneration.getChunkVein(worldIn, playerIn.getPosition());
            String str = stack.getItem().getItemStackDisplayName(stack) + " " + stack.getCount();
            if (stack.isEmpty()) str = ItemProspectingPan.notFound;
            Utils.sendChatMessage(playerIn, str);
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
