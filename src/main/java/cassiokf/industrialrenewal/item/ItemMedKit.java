package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemMedKit extends ItemBase
{

    public ItemMedKit(String name, CreativeTabs tab)
    {
        super(name, tab);
        maxStackSize = 16;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.shouldHeal() && !player.isPotionActive(MobEffects.REGENERATION))
        {
            if (!worldIn.isRemote)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, IRConfig.MainConfig.Main.medKitEffectDuration, 1, false, false));
            }
            itemstack.shrink(1);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag)
    {
        list.add(I18n.format("item." + References.MODID + "." + name + ".des0"));
    }
}
