package cassiokf.industrialrenewal.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;

public class CommonProxy {

    public void Init() {
        //Need to put in Main class
    }

    public void preInit() {

    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    public void registerItemRenderer(Block block, int meta, String id) {
        registerItemRenderer(Item.getItemFromBlock(block), meta, id);
    }

    public void registerBlockRenderers() {

    }

    public void registerRenderers() {
    }

    public String localize(String unlocalized, Object... args) {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }
}