package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IndustrialRenewal.creativeTab);
    }

    public void registerItemModel() {
        IndustrialRenewal.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}