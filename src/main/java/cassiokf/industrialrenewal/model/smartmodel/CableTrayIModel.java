package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.CableTrayComposite;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

public class CableTrayIModel implements IModel
{
    public static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("industrialrenewal:blocks/data_cable");

    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:cable_tray/tray_core");
    public static final ModelResourceLocation MODEL_MASTER = new ModelResourceLocation("industrialrenewal:cable_tray/cable_master");

    //plug to close tray side
    public static final ModelResourceLocation MODEL_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/con_north");
    public static final ModelResourceLocation MODEL_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/con_south");
    public static final ModelResourceLocation MODEL_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/con_west");
    public static final ModelResourceLocation MODEL_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/con_east");

    //Connection between two trays
    public static final ModelResourceLocation MODEL2_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/con2_north");
    public static final ModelResourceLocation MODEL2_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/con2_south");
    public static final ModelResourceLocation MODEL2_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/con2_west");
    public static final ModelResourceLocation MODEL2_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/con2_east");
    public static final ModelResourceLocation MODEL2_UP = new ModelResourceLocation("industrialrenewal:cable_tray/con2_up");
    public static final ModelResourceLocation MODEL2_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/con2_down");

    //Tray with pipe to tray with pipe connection
    public static final ModelResourceLocation MODEL_PIPE_CORE = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_core");
    public static final ModelResourceLocation MODEL_PIPE_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con_south");
    public static final ModelResourceLocation MODEL_PIPE_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con_north");
    public static final ModelResourceLocation MODEL_PIPE_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con_west");
    public static final ModelResourceLocation MODEL_PIPE_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con_east");
    public static final ModelResourceLocation MODEL_PIPE_UP = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con_up");
    public static final ModelResourceLocation MODEL_PIPE_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con_down");

    //Tray with pipe to Fluid pipe connection
    public static final ModelResourceLocation MODEL2_PIPE_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con2_south");
    public static final ModelResourceLocation MODEL2_PIPE_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con2_north");
    public static final ModelResourceLocation MODEL2_PIPE_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con2_west");
    public static final ModelResourceLocation MODEL2_PIPE_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con2_east");
    public static final ModelResourceLocation MODEL2_PIPE_UP = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con2_up");
    public static final ModelResourceLocation MODEL2_PIPE_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/pipe/pipe_con2_down");

    //Tray with energy to tray with energy connection
    public static final ModelResourceLocation MODEL_HV_CORE = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_core");
    public static final ModelResourceLocation MODEL_HV_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con_south");
    public static final ModelResourceLocation MODEL_HV_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con_north");
    public static final ModelResourceLocation MODEL_HV_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con_west");
    public static final ModelResourceLocation MODEL_HV_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con_east");
    public static final ModelResourceLocation MODEL_HV_UP = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con_up");
    public static final ModelResourceLocation MODEL_HV_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con_down");

    public static final ModelResourceLocation MODEL_MV_CORE = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_core");
    public static final ModelResourceLocation MODEL_MV_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con_south");
    public static final ModelResourceLocation MODEL_MV_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con_north");
    public static final ModelResourceLocation MODEL_MV_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con_west");
    public static final ModelResourceLocation MODEL_MV_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con_east");
    public static final ModelResourceLocation MODEL_MV_UP = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con_up");
    public static final ModelResourceLocation MODEL_MV_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con_down");

    public static final ModelResourceLocation MODEL_LV_CORE = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_core");
    public static final ModelResourceLocation MODEL_LV_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con_south");
    public static final ModelResourceLocation MODEL_LV_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con_north");
    public static final ModelResourceLocation MODEL_LV_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con_west");
    public static final ModelResourceLocation MODEL_LV_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con_east");
    public static final ModelResourceLocation MODEL_LV_UP = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con_up");
    public static final ModelResourceLocation MODEL_LV_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con_down");

    //Tray with energy to Energy Cable connection
    public static final ModelResourceLocation MODEL2_HV_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con2_south");
    public static final ModelResourceLocation MODEL2_HV_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con2_north");
    public static final ModelResourceLocation MODEL2_HV_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con2_west");
    public static final ModelResourceLocation MODEL2_HV_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con2_east");
    public static final ModelResourceLocation MODEL2_HV_UP = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con2_up");
    public static final ModelResourceLocation MODEL2_HV_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/energy/hv/pipe_con2_down");

    public static final ModelResourceLocation MODEL2_MV_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con2_south");
    public static final ModelResourceLocation MODEL2_MV_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con2_north");
    public static final ModelResourceLocation MODEL2_MV_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con2_west");
    public static final ModelResourceLocation MODEL2_MV_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con2_east");
    public static final ModelResourceLocation MODEL2_MV_UP = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con2_up");
    public static final ModelResourceLocation MODEL2_MV_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/energy/mv/pipe_con2_down");

    public static final ModelResourceLocation MODEL2_LV_SOUTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con2_south");
    public static final ModelResourceLocation MODEL2_LV_NORTH = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con2_north");
    public static final ModelResourceLocation MODEL2_LV_WEST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con2_west");
    public static final ModelResourceLocation MODEL2_LV_EAST = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con2_east");
    public static final ModelResourceLocation MODEL2_LV_UP = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con2_up");
    public static final ModelResourceLocation MODEL2_LV_DOWN = new ModelResourceLocation("industrialrenewal:cable_tray/energy/lv/pipe_con2_down");

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        try
        {
            IModel subComponent = ModelLoaderRegistry.getModel(MODEL_CORE);
            IBakedModel bakedModelCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MASTER);
            IBakedModel bakedModelMaster = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_WEST);
            IBakedModel bakedModelWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_EAST);
            IBakedModel bakedModelEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_NORTH);
            IBakedModel bakedModelNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_SOUTH);
            IBakedModel bakedModelSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_DOWN);
            IBakedModel bakedModelDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_UP);
            IBakedModel bakedModelUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_WEST);
            IBakedModel bakedModel2West = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_EAST);
            IBakedModel bakedModel2East = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_NORTH);
            IBakedModel bakedModel2North = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_SOUTH);
            IBakedModel bakedModel2South = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_CORE);
            IBakedModel bakedModelPipeCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_SOUTH);
            IBakedModel bakedModelPipeSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_NORTH);
            IBakedModel bakedModelPipeNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_EAST);
            IBakedModel bakedModelPipeEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_WEST);
            IBakedModel bakedModelPipeWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_UP);
            IBakedModel bakedModelPipeUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_PIPE_DOWN);
            IBakedModel bakedModelPipeDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_PIPE_SOUTH);
            IBakedModel bakedModel2PipeSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_PIPE_NORTH);
            IBakedModel bakedModel2PipeNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_PIPE_EAST);
            IBakedModel bakedModel2PipeEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_PIPE_WEST);
            IBakedModel bakedModel2PipeWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_PIPE_UP);
            IBakedModel bakedModel2PipeUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_PIPE_DOWN);
            IBakedModel bakedModel2PipeDown = subComponent.bake(state, format, bakedTextureGetter);

            //HV

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_CORE);
            IBakedModel bakedModelHvCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_SOUTH);
            IBakedModel bakedModelHvSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_NORTH);
            IBakedModel bakedModelHvNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_EAST);
            IBakedModel bakedModelHvEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_WEST);
            IBakedModel bakedModelHvWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_UP);
            IBakedModel bakedModelHvUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_HV_DOWN);
            IBakedModel bakedModelHvDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_HV_SOUTH);
            IBakedModel bakedModel2HvSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_HV_NORTH);
            IBakedModel bakedModel2HvNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_HV_EAST);
            IBakedModel bakedModel2HvEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_HV_WEST);
            IBakedModel bakedModel2HvWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_HV_UP);
            IBakedModel bakedModel2HvUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_HV_DOWN);
            IBakedModel bakedModel2HvDown = subComponent.bake(state, format, bakedTextureGetter);

            //MV

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_CORE);
            IBakedModel bakedModelMvCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_SOUTH);
            IBakedModel bakedModelMvSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_NORTH);
            IBakedModel bakedModelMvNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_EAST);
            IBakedModel bakedModelMvEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_WEST);
            IBakedModel bakedModelMvWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_UP);
            IBakedModel bakedModelMvUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_MV_DOWN);
            IBakedModel bakedModelMvDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_MV_SOUTH);
            IBakedModel bakedModel2MvSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_MV_NORTH);
            IBakedModel bakedModel2MvNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_MV_EAST);
            IBakedModel bakedModel2MvEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_MV_WEST);
            IBakedModel bakedModel2MvWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_MV_UP);
            IBakedModel bakedModel2MvUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_MV_DOWN);
            IBakedModel bakedModel2MvDown = subComponent.bake(state, format, bakedTextureGetter);

            //LV

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_CORE);
            IBakedModel bakedModelLvCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_SOUTH);
            IBakedModel bakedModelLvSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_NORTH);
            IBakedModel bakedModelLvNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_EAST);
            IBakedModel bakedModelLvEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_WEST);
            IBakedModel bakedModelLvWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_UP);
            IBakedModel bakedModelLvUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL_LV_DOWN);
            IBakedModel bakedModelLvDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_LV_SOUTH);
            IBakedModel bakedModel2LvSouth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_LV_NORTH);
            IBakedModel bakedModel2LvNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_LV_EAST);
            IBakedModel bakedModel2LvEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_LV_WEST);
            IBakedModel bakedModel2LvWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_LV_UP);
            IBakedModel bakedModel2LvUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(MODEL2_LV_DOWN);
            IBakedModel bakedModel2LvDown = subComponent.bake(state, format, bakedTextureGetter);

            return new CableTrayComposite(bakedModelCore, bakedModelMaster, bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth,
                    bakedModelUp, bakedModelDown, bakedModel2West, bakedModel2East, bakedModel2North, bakedModel2South,
                    bakedModelPipeCore, bakedModelPipeNorth, bakedModelPipeSouth, bakedModelPipeEast, bakedModelPipeWest,
                    bakedModelPipeUp, bakedModelPipeDown, bakedModel2PipeNorth, bakedModel2PipeSouth, bakedModel2PipeEast,
                    bakedModel2PipeWest, bakedModel2PipeUp, bakedModel2PipeDown,
                    bakedModelHvCore, bakedModelHvNorth, bakedModelHvSouth, bakedModelHvEast, bakedModelHvWest,
                    bakedModelHvUp, bakedModelHvDown, bakedModel2HvNorth, bakedModel2HvSouth, bakedModel2HvEast,
                    bakedModel2HvWest, bakedModel2HvUp, bakedModel2HvDown,
                    bakedModelMvCore, bakedModelMvNorth, bakedModelMvSouth, bakedModelMvEast, bakedModelMvWest,
                    bakedModelMvUp, bakedModelMvDown, bakedModel2MvNorth, bakedModel2MvSouth, bakedModel2MvEast,
                    bakedModel2MvWest, bakedModel2MvUp, bakedModel2MvDown,
                    bakedModelLvCore, bakedModelLvNorth, bakedModelLvSouth, bakedModelLvEast, bakedModelLvWest,
                    bakedModelLvUp, bakedModelLvDown, bakedModel2LvNorth, bakedModel2LvSouth, bakedModel2LvEast,
                    bakedModel2LvWest, bakedModel2LvUp, bakedModel2LvDown);
        } catch (Exception exception)
        {
            System.err.println("WebModel.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.copyOf(new ResourceLocation[]{MODEL_CORE, MODEL_WEST, MODEL_EAST, MODEL_NORTH, MODEL_SOUTH,
                MODEL2_WEST, MODEL2_EAST, MODEL2_NORTH, MODEL2_SOUTH});
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return ImmutableList.copyOf(new ResourceLocation[]{TEXTURE_SHEET});
    }
}
