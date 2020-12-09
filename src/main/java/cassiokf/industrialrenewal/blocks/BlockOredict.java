package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOredict extends BlockBase
{
    private final String oreName;

    public BlockOredict(Material material, String name, String oreName, CreativeTabs tab)
    {
        super(material, name, tab);

        this.oreName = oreName;
    }

    public void initOreDict()
    {
        OreDictionary.registerOre(oreName, this);
    }
}
