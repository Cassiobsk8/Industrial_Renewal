package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemProspectingPan extends ItemBase
{
    private static final String found = "Deep vein found size:";
    public static final String notFound = "No deep vein found";

    public ItemProspectingPan(Item.Properties properties)
    {
        super(properties.maxStackSize(1).maxDamage(13));
        this.addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter()
        {
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn)
            {
                return ItemProspectingPan.isEmpty(stack) ? 0.0F : 1.0F;
            }
        });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.prospectingpan.info")));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static boolean isEmpty(ItemStack stack)
    {
        return stack.getDamage() == 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack resourceItem)
    {
        return false;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (context.getWorld().getDimension().getType() == DimensionType.OVERWORLD && stack.getDamage() <= 0)
        {
            if (!context.getWorld().isRemote) stack.setDamage(1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.getDimension().getType() == DimensionType.OVERWORLD && stack.getDamage() > 0)
        {
            playerIn.swingArm(handIn);
            prospect(worldIn, playerIn, stack);
            return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private static void prospect(World worldIn, PlayerEntity playerIn, ItemStack panStack)
    {
        if (worldIn.isRemote) return;
        panStack.attemptDamageItem(1,new Random(), (ServerPlayerEntity) playerIn);
        if (panStack.getDamage() >= 12)
        {
            ItemStack stackV = OreGeneration.getChunkVein(worldIn, playerIn.getPosition());

            boolean hasVein = !stackV.isEmpty();
            String msg;
            if (hasVein) msg = stackV.getDisplayName()
                    + " "
                    + found
                    + " "
                    + stackV.getCount();
            else msg = notFound;

            Utils.sendChatMessage(playerIn, msg);
            panStack.setDamage(0);
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        int dmg = stack.getDamage();
        if (dmg == getMaxDamage(stack))
        {
            return new ItemStack(stack.getItem(), 0, stack.getTag());
        }
        ItemStack tr = new ItemStack(stack.getItem(), 1, stack.getTag());
        tr.setDamage(dmg + 1);
        return tr;
    }
}
