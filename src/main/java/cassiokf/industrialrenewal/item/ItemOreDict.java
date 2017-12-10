package cassiokf.industrialrenewal.item;

import net.minecraftforge.oredict.OreDictionary;

public class ItemOreDict extends ItemBase {

    private String oreName;

    public ItemOreDict(String name, String oreName) {
        super(name);

        this.oreName = oreName;
    }

    public void initOreDict() {
        OreDictionary.registerOre(oreName, this);
    }

}
