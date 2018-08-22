package cassiokf.industrialrenewal.item.armor;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.armor.model.SafetyBeltModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemSafetyBelt extends ItemArmor {

    protected String name;

    public ItemSafetyBelt(String name, CreativeTabs tab) {
        super(ArmorMaterial.CHAIN, 3, EntityEquipmentSlot.CHEST);
        this.name = name;
        this.setRegistryName(References.MODID, name);
        this.setUnlocalizedName(References.MODID + "." + name);
        this.setCreativeTab(tab);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            stack.addEnchantment(Enchantment.getEnchantmentByID(2), 4);
            items.add(stack);
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        stack.addEnchantment(Enchantment.getEnchantmentByID(2), 4);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }

    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "industrialrenewal:textures/armor/safety_belt.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nullable
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return new SafetyBeltModel();
    }

    public void registerItemModel() {
        IndustrialRenewal.proxy.registerItemRenderer(this, 0, name);
    }

}
