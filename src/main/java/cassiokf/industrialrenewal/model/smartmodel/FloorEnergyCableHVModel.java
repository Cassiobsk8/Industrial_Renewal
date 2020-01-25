package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.PillarBaseComposite;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.function.Function;

public class FloorEnergyCableHVModel implements IModel
{
    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/pipe_core");
    public static final ModelResourceLocation MODEL_MASTER = new ModelResourceLocation("industrialrenewal:pipe_energy/cable_master");

    public static final ModelResourceLocation MODEL_DOWN = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con_down");
    public static final ModelResourceLocation MODEL_UP = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con_up");
    public static final ModelResourceLocation MODEL_NORTH = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con_north");
    public static final ModelResourceLocation MODEL_SOUTH = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con_south");
    public static final ModelResourceLocation MODEL_WEST = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con_west");
    public static final ModelResourceLocation MODEL_EAST = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con_east");

    public static final ModelResourceLocation MODEL2_DOWN = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con2_down");
    public static final ModelResourceLocation MODEL2_UP = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con2_up");
    public static final ModelResourceLocation MODEL2_NORTH = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con2_north");
    public static final ModelResourceLocation MODEL2_SOUTH = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con2_south");
    public static final ModelResourceLocation MODEL2_WEST = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con2_west");
    public static final ModelResourceLocation MODEL2_EAST = new ModelResourceLocation("industrialrenewal:pipe_energy_hv/con2_east");

    public static final ModelResourceLocation MODEL3_DOWN = new ModelResourceLocation("industrialrenewal:indfloor/con_down");
    public static final ModelResourceLocation MODEL3_UP = new ModelResourceLocation("industrialrenewal:indfloor/con_up");
    public static final ModelResourceLocation MODEL3_NORTH = new ModelResourceLocation("industrialrenewal:indfloor/con_north");
    public static final ModelResourceLocation MODEL3_SOUTH = new ModelResourceLocation("industrialrenewal:indfloor/con_south");
    public static final ModelResourceLocation MODEL3_WEST = new ModelResourceLocation("industrialrenewal:indfloor/con_west");
    public static final ModelResourceLocation MODEL3_EAST = new ModelResourceLocation("industrialrenewal:indfloor/con_east");

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        try
        {
            IModel subComponent = ModelLoaderRegistry.getModel(MODEL_CORE);
            IBakedModel bakedModelCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MASTER);
            IBakedModel bakedModelMaster = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_DOWN);
            IBakedModel bakedModelDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_UP);
            IBakedModel bakedModelUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_WEST);
            IBakedModel bakedModelWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_EAST);
            IBakedModel bakedModelEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_NORTH);
            IBakedModel bakedModelNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_SOUTH);
            IBakedModel bakedModelSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_DOWN);
            IBakedModel bakedModel2Down = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_UP);
            IBakedModel bakedModel2Up = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_WEST);
            IBakedModel bakedModel2West = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_EAST);
            IBakedModel bakedModel2East = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_NORTH);
            IBakedModel bakedModel2North = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_SOUTH);
            IBakedModel bakedModel2South = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL3_DOWN);
            IBakedModel bakedModel3Down = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL3_UP);
            IBakedModel bakedModel3Up = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL3_WEST);
            IBakedModel bakedModel3West = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL3_EAST);
            IBakedModel bakedModel3East = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL3_NORTH);
            IBakedModel bakedModel3North = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL3_SOUTH);
            IBakedModel bakedModel3South = subComponent.bake(state, format, bakedTextureGetter);

            return new PillarBaseComposite(bakedModelCore, bakedModelMaster, bakedModelDown, bakedModelUp, bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth,
                    bakedModel2Down, bakedModel2Up, bakedModel2West, bakedModel2East, bakedModel2North, bakedModel2South,
                    bakedModel3Down, bakedModel3Up, bakedModel3West, bakedModel3East, bakedModel3North, bakedModel3South);
        } catch (Exception exception)
        {
            System.err.println("WebModel.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }
}
