package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemDiscBase extends ItemRecord {

    protected String name;

    protected ItemDiscBase(String name, CreativeTabs tab, SoundEvent sound) {
        super(name, sound);
        this.name = name;
        this.setRegistryName(References.MODID, name);
        this.setUnlocalizedName(References.MODID + "." + name);
        this.setCreativeTab(tab);
    }

    public void initOreDict() {
        OreDictionary.registerOre("record", this);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getRecordNameLocal() {
        return I18n.format("item." + References.MODID + "." + name + ".des0");
    }

    public void registerItemModel() {
        IndustrialRenewal.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemDiscBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
