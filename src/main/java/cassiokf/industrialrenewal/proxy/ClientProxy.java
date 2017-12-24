package cassiokf.industrialrenewal.proxy;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.tileentity.TileEntitySRender;
import cassiokf.industrialrenewal.tileentity.carts.TileEntityCartCargoContainer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(IndustrialRenewal.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerBlockRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(TileEntityCartCargoContainer.class, TileEntitySRender::new);
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }
}