package cassiokf.industrialrenewal.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOreDict extends ItemBase {

    private String oreName;

    public ItemOreDict(String name, String oreName, CreativeTabs tab) {
        super(name, tab);

        this.oreName = oreName;
    }

    public void initOreDict() {
        OreDictionary.registerOre(oreName, this);
    }

}
