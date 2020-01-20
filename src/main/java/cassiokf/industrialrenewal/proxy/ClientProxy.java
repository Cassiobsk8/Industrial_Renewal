package cassiokf.industrialrenewal.proxy;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.RenderHandler;
import cassiokf.industrialrenewal.model.ModelLoaderCustom;
import cassiokf.industrialrenewal.tesr.*;
import cassiokf.industrialrenewal.tileentity.*;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableGauge;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeGauge;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void Init() {
        super.Init();
        MinecraftForge.EVENT_BUS.register(IRSoundHandler.class);
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
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFirstAidKit.class, new TESRFirstAidKit());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGauge.class, new TESRGauge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBatteryBank.class, new TESRBatteryBank());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamBoiler.class, new TESRSteamBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamTurbine.class, new TESRSteamTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallWindTurbine.class, new TESRSmallWindTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbinePillar.class, new TESRWindTurbinePillar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBulkConveyor.class, new TESRBulkConveyor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMining.class, new TESRMining());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolarPanelFrame.class, new TESRSolarPanelFrame());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidPipeGauge.class, new TESRPipeGauge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyCableGauge.class, new TESRCableGauge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyLevel.class, new TESREnergyFill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidLoader.class, new TESRFluidLoader());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCargoLoader.class, new TESRCargoLoader());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWireBase.class, new TESRWire());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransformerHV.class, new TESRTransformerHV());

        ModelLoaderRegistry.registerLoader(new ModelLoaderCustom());
    }
}