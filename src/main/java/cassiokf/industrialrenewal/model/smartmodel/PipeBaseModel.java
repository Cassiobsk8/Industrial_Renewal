package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.PipeBaseComposite;
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

public class PipeBaseModel implements IModel
{
    public static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("industrialrenewal:blocks/anvil_base");

    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:pipe_fluid/pipe_core");

    public static final ModelResourceLocation MODEL_DOWN = new ModelResourceLocation("industrialrenewal:pipe_fluid/con_down");
    public static final ModelResourceLocation MODEL_UP = new ModelResourceLocation("industrialrenewal:pipe_fluid/con_up");
    public static final ModelResourceLocation MODEL_NORTH = new ModelResourceLocation("industrialrenewal:pipe_fluid/con_north");
    public static final ModelResourceLocation MODEL_SOUTH = new ModelResourceLocation("industrialrenewal:pipe_fluid/con_south");
    public static final ModelResourceLocation MODEL_WEST = new ModelResourceLocation("industrialrenewal:pipe_fluid/con_west");
    public static final ModelResourceLocation MODEL_EAST = new ModelResourceLocation("industrialrenewal:pipe_fluid/con_east");

    public static final ModelResourceLocation MODEL2_DOWN = new ModelResourceLocation("industrialrenewal:pipe_fluid/con2_down");
    public static final ModelResourceLocation MODEL2_UP = new ModelResourceLocation("industrialrenewal:pipe_fluid/con2_up");
    public static final ModelResourceLocation MODEL2_NORTH = new ModelResourceLocation("industrialrenewal:pipe_fluid/con2_north");
    public static final ModelResourceLocation MODEL2_SOUTH = new ModelResourceLocation("industrialrenewal:pipe_fluid/con2_south");
    public static final ModelResourceLocation MODEL2_WEST = new ModelResourceLocation("industrialrenewal:pipe_fluid/con2_west");
    public static final ModelResourceLocation MODEL2_EAST = new ModelResourceLocation("industrialrenewal:pipe_fluid/con2_east");

    // return all other resources used by this model (not strictly needed for this example because we load all the subcomponent
    //   models during the bake anyway)
    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.copyOf(new ResourceLocation[]{MODEL_CORE, MODEL_DOWN, MODEL_UP, MODEL_WEST, MODEL_EAST, MODEL_NORTH, MODEL_SOUTH,
                MODEL2_DOWN, MODEL2_UP, MODEL2_WEST, MODEL2_EAST, MODEL2_NORTH, MODEL2_SOUTH});
    }

    // return all the textures used by this model (not strictly needed for this example because we load all the subcomponent
    //   models during the bake anyway)
    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return ImmutableList.copyOf(new ResourceLocation[]{TEXTURE_SHEET});
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        try
        {
            IModel subComponent = ModelLoaderRegistry.getModel(MODEL_CORE);
            IBakedModel bakedModelCore = subComponent.bake(state, format, bakedTextureGetter);

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

            return new PipeBaseComposite(bakedModelCore, bakedModelDown, bakedModelUp, bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth,
                    bakedModel2Down, bakedModel2Up, bakedModel2West, bakedModel2East, bakedModel2North, bakedModel2South);
        } catch (Exception exception)
        {
            System.err.println("WebModel.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }
}
