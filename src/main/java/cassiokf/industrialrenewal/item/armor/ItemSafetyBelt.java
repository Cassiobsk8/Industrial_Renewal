package cassiokf.industrialrenewal.item.armor;

import cassiokf.industrialrenewal.model.armor.SafetyBeltModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemSafetyBelt extends ArmorItem
{
    public ItemSafetyBelt(Item.Properties properties)
    {
        super(ArmorMaterial.CHAIN, EquipmentSlotType.CHEST, properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            ItemStack stack = new ItemStack(this);
            stack.addEnchantment(Enchantment.getEnchantmentByID(2), 5);
            items.add(stack);
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn)
    {
        stack.addEnchantment(Enchantment.getEnchantmentByID(2), 5);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        return "industrialrenewal:textures/armor/safety_belt.png";
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default)
    {
        return (A) new SafetyBeltModel();
    }
}
