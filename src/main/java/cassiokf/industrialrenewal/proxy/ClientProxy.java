package cassiokf.industrialrenewal.proxy;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.RenderHandler;
import cassiokf.industrialrenewal.tesr.*;
import cassiokf.industrialrenewal.tileentity.*;
import cassiokf.industrialrenewal.tileentity.machines.steamboiler.TESRSteamBoiler;
import cassiokf.industrialrenewal.tileentity.machines.steamboiler.TileEntitySteamBoiler;
import cassiokf.industrialrenewal.tileentity.machines.steamturbine.TESRSteamTurbine;
import cassiokf.industrialrenewal.tileentity.machines.steamturbine.TileEntitySteamTurbine;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void Init() {
        super.Init();
    }

    @Override
    public void preInit() {
        //IRConfig.clientPreInit();
        RenderHandler.registerEntitiesRender();
        RenderHandler.registerCustomMeshesAndStates();
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
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFirstAidKit.class, new TESRFirstAidKit());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGauge.class, new TESRGauge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBatteryBank.class, new TESRBatteryBank());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamBoiler.class, new TESRSteamBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamTurbine.class, new TESRSteamTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallWindTurbine.class, new TESRSmallWindTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbinePillar.class, new TESRWindTurbinePillar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBulkConveyor.class, new TESRBulkConveyor());
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }
}