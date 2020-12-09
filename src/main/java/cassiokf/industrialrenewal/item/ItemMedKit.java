package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ItemMedKit extends ItemBase {


    public ItemMedKit(Properties properties)
    {
        super(properties.maxStackSize(16));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.shouldHeal() && !player.isPotionActive(Effects.REGENERATION)) {
            if (!worldIn.isRemote) {
                player.addPotionEffect(new Effect(Effects.REGENERATION, IRConfig.Main.medKitEffectDuration.get(), 1, false, false));
            }
            itemstack.shrink(1);
        }
        return new ActionResult<ItemStack>(ActionResultType.PASS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        list.add(new StringTextComponent(I18n.format("item." + References.MODID + "." + name + ".des0")));
    }
}
