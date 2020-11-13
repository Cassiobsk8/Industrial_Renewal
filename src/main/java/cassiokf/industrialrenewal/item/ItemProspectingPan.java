package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.world.generation.OreGeneration;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemProspectingPan extends ItemBase
{
    private static final String found = "Deep vein found size:";
    public static final String notFound = "No deep vein found";

    public ItemProspectingPan(String name, CreativeTabs tab)
    {
        super(name, tab);
        this.maxStackSize = 1;
        this.setMaxDamage(13);
        setContainerItem(this);
        this.addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return ItemProspectingPan.isEmpty(stack) ? 0.0F : 1.0F;
            }
        });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("info.industrialrenewal.prospectingpan.info"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static boolean isEmpty(ItemStack stack)
    {
        return stack.getItemDamage() == 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack resourceItem)
    {
        return false;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (worldIn.provider.getDimension() == 0 && stack.getItemDamage() <= 0)
        {
            if (!worldIn.isRemote) stack.setItemDamage(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.provider.getDimension() == 0 && stack.getItemDamage() > 0)
        {
            playerIn.swingArm(handIn);
            prospect(worldIn, playerIn, stack);
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void prospect(World worldIn, EntityPlayer playerIn, ItemStack stack)
    {
        if (worldIn.isRemote) return;
        stack.damageItem(1, playerIn);
        if (stack.getItemDamage() >= 12)
        {
            ItemStack stackv = OreGeneration.getChunkVein(worldIn, playerIn.getPosition());

            boolean hasVein = !stackv.isEmpty();
            String msg;
            if (hasVein) msg = stackv.getDisplayName()
                    + " "
                    + found
                    + " "
                    + stackv.getCount();
            else msg = notFound;

            Utils.sendChatMessage(playerIn, msg);
            stack.setItemDamage(0);
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        int dmg = stack.getItemDamage();
        if (dmg == getMaxDamage(stack))
        {
            return new ItemStack(stack.getItem(), 0, getMaxDamage(stack));
        }
        ItemStack tr = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
        tr.setItemDamage(dmg + 1);
        return tr;
    }
}
