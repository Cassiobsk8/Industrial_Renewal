package cassiokf.industrialrenewal.proxy;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.RenderHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        IRConfig.clientPreInit();
        RenderHandler.registerEntitiesRender();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(References.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerBlockRenderers() {
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFilter.class, new FilterTESRender());
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }
}