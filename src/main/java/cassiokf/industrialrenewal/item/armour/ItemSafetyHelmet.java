package cassiokf.industrialrenewal.item.armour;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.armour.model.SafetyHelmetModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemSafetyHelmet extends ItemArmor {

    protected String name;

    public ItemSafetyHelmet(String name, CreativeTabs tab) {
        super(ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD);
        this.name = name;
        this.setRegistryName(References.MODID, name);
        this.setUnlocalizedName(References.MODID + "." + name);
        this.setCreativeTab(tab);
    }

    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "industrialrenewal:textures/armour/safety_helmet.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nullable
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return new SafetyHelmetModel();
    }

    public void registerItemModel() {
        IndustrialRenewal.proxy.registerItemRenderer(this, 0, name);
    }

}
