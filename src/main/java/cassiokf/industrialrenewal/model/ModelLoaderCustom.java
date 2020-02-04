package cassiokf.industrialrenewal.model;

import cassiokf.industrialrenewal.model.smartmodel.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ModelLoaderCustom implements ICustomModelLoader
{
    public final String SMART_MODEL_RESOURCE_LOCATION = "models/block/smartmodel/";
    private IResourceManager resourceManager;

    // return true if our Model Loader accepts this ModelResourceLocation
    @Override
    public boolean accepts(ResourceLocation resourceLocation)
    {
        return resourceLocation.getNamespace().equals("industrialrenewal")
                && resourceLocation.getPath().startsWith(SMART_MODEL_RESOURCE_LOCATION);
    }

    // When called for our Block3DWeb's ModelResourceLocation, return our WebModel.
    @Override
    public IModel loadModel(ResourceLocation resourceLocation)
    {
        String resourcePath = resourceLocation.getPath();
        assert resourcePath.startsWith(SMART_MODEL_RESOURCE_LOCATION) : "loadModel expected " + SMART_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;

        String modelName = resourcePath.substring(SMART_MODEL_RESOURCE_LOCATION.length());

        switch (modelName)
        {
            case "roofmodel":
                return new RoofModel();
            case "cable_tray":
                return new CableTrayIModel();
            case "pipe_fluid":
                return new PipeBaseModel();
            case "pipe_energy":
                return new EnergyCableBaseModel();
            case "pipe_energy_hv":
                return new EnergyCableHVBaseModel();
            case "pipe_energy_lv":
                return new EnergyCableLVBaseModel();
            case "pillar_energy":
                return new PillarEnergyCableBaseModel();
            case "pillar_energy_lv":
                return new PillarEnergyCableLVBaseModel();
            case "pillar_energy_hv":
                return new PillarEnergyCableHVBaseModel();
            case "pillar_fluid":
                return new PillarFluidPipeBaseModel();
            case "pipe_energy_gauge":
                return new GaugeEnergyCableBaseModel();
            case "pipe_energy_gauge_lv":
                return new GaugeEnergyCableLVBaseModel();
            case "pipe_energy_gauge_hv":
                return new GaugeEnergyCableHVBaseModel();
            case "pipe_fluid_gauge":
                return new GaugeFluidPipeBaseModel();
            case "indfloor":
                return new IndFloorModel();
            case "indfloor_lamp":
                return new IndFloorLampModel();
            case "floor_cable":
                return new FloorEnergyCableModel();
            case "floor_cable_lv":
                return new FloorEnergyCableLVModel();
            case "floor_cable_hv":
                return new FloorEnergyCableHVModel();
            case "floor_pipe":
                return new FloorFluidPipeIModel();
            case "battery_bank":
                return new BatteryBankIModel();
            case "turbine_pillar":
                return new TurbinePillarIModel();
            case "fence_big_column":
                return new BigFenceColumnIModel();
            case "fence_big_corner":
                return new BigFenceCornerIModel();
            default:
                return ModelLoaderRegistry.getMissingModel();
        }
    }

    // don't need it for this example; you might.  We have to implement it anyway.
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.resourceManager = resourceManager;
    }
}
