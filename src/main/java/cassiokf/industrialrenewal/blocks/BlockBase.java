package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.References;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


public class BlockBase extends Block
{

    protected String name;
    protected ItemGroup group;

    public BlockBase(Material material, String name, ItemGroup group)
    {
        super(Block.Properties.create(material).hardnessAndResistance(2f, 5f));
        this.name = name;
        this.group = group;
        setRegistryName(References.MODID, name);
    }

    /*public void registerItemModel(Item itemBlock) {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }
*/
    public Item createItemBlock()
    {
        return new BlockItem(this, new Item.Properties().group(group)).setRegistryName(References.MODID, name);
    }
}