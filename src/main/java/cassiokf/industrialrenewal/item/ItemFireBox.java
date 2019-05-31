package cassiokf.industrialrenewal.item;

import net.minecraft.creativetab.CreativeTabs;

public class ItemFireBox extends ItemBase
{

    public int type;

    public ItemFireBox(String name, int type, CreativeTabs tab)
    {
        super(name, tab);
        this.type = type;
    }
}
