package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemBase extends Item
{

    protected String name;

    public ItemBase(String name, ItemGroup group)
    {
        super(new Item.Properties().group(group));
        this.name = name;
        setRegistryName(References.MODID, name);
    }

    /*public void registerItemModel() {
        IndustrialRenewal.registerItemRenderer(this, 0, name);
    }*/


}