package cassiokf.industrialrenewal.model;

import cassiokf.industrialrenewal.model.smartmodel.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class ModelLoaderCustom implements IModelLoader<BaseModelGeometry>
{

    @Override
    public BaseModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
    {
        String modelName = modelContents.get("name").getAsString();
        switch (modelName)
        {
            default:
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
            //case "indfloor":
            //    return new IndFloorModel();
            //case "indfloor_lamp":
            //    return new IndFloorLampModel();
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
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
    }
}
