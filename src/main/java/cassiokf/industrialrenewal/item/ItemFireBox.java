package cassiokf.industrialrenewal.item;

import net.minecraft.item.Item;

public class ItemFireBox extends ItemBase
{

    public int type;

    public ItemFireBox(Item.Properties properties, int type)
    {
        super(properties);
        this.type = type;
    }
}
