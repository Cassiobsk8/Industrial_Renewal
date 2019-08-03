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

        if (modelName.equals("roofmodel"))
        {
            return new RoofModel();
        } else if (modelName.equals("pipe_fluid"))
        {
            return new PipeBaseModel();
        } else if (modelName.equals("pipe_energy"))
        {
            return new EnergyCableBaseModel();
        } else if (modelName.equals("pillar_energy"))
        {
            return new PillarEnergyCableBaseModel();
        } else if (modelName.equals("pillar_fluid"))
        {
            return new PillarFluidPipeBaseModel();
        } else if (modelName.equals("pipe_energy_gauge"))
        {
            return new GaugeEnergyCableBaseModel();
        } else if (modelName.equals("pipe_fluid_gauge"))
        {
            return new GaugeFluidPipeBaseModel();
        } else if (modelName.equals("indfloor"))
        {
            return new IndFloorModel();
        } else
        {
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
