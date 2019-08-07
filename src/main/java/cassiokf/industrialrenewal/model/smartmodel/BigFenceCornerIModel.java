package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.BigFenceComposite;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.function.Function;

public class BigFenceCornerIModel implements IModel
{
    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_corner_core");

    public static final ModelResourceLocation ACTIVE_LEFT = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_front");
    public static final ModelResourceLocation ACTIVE_RIGHT = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_east");
    public static final ModelResourceLocation ACTIVE_LEFT_TOP = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_top_f");
    public static final ModelResourceLocation ACTIVE_RIGHT_TOP = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_top");
    public static final ModelResourceLocation ACTIVE_LEFT_DOWN = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_down_f");
    public static final ModelResourceLocation ACTIVE_RIGHT_DOWN = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_down_r");

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        try
        {
            IModel subComponent = ModelLoaderRegistry.getModel(MODEL_CORE);
            IBakedModel bakedModelCore = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(ACTIVE_LEFT);
            IBakedModel bakedModelDown = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(ACTIVE_RIGHT);
            IBakedModel bakedModelUp = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(ACTIVE_LEFT_DOWN);
            IBakedModel bakedModelWest = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(ACTIVE_RIGHT_DOWN);
            IBakedModel bakedModelEast = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(ACTIVE_LEFT_TOP);
            IBakedModel bakedModelNorth = subComponent.bake(state, format, bakedTextureGetter);

            subComponent = ModelLoaderRegistry.getModel(ACTIVE_RIGHT_TOP);
            IBakedModel bakedModelSouth = subComponent.bake(state, format, bakedTextureGetter);

            return new BigFenceComposite(bakedModelCore, bakedModelDown, bakedModelUp, bakedModelWest, bakedModelEast,
                    bakedModelNorth, bakedModelSouth);
        } catch (Exception exception)
        {
            System.err.println("WebModel.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }
}
