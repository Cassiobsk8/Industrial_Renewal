package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.BigFenceComposite;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Function;

public class BigFenceColumnIModel extends BaseModelGeometry
{
    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_core");

    public static final ModelResourceLocation ACTIVE_LEFT = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_west");
    public static final ModelResourceLocation ACTIVE_RIGHT = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_east");
    public static final ModelResourceLocation ACTIVE_LEFT_TOP = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_top_l");
    public static final ModelResourceLocation ACTIVE_RIGHT_TOP = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_top");
    public static final ModelResourceLocation ACTIVE_LEFT_DOWN = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_down_l");
    public static final ModelResourceLocation ACTIVE_RIGHT_DOWN = new ModelResourceLocation("industrialrenewal:fence_big/fence_big_side_down_r");


    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        IUnbakedModel subComponent = ModelLoader.instance().getUnbakedModel(MODEL_CORE);
        IBakedModel bakedModelCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(ACTIVE_LEFT);
        IBakedModel bakedModelDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(ACTIVE_RIGHT);
        IBakedModel bakedModelUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(ACTIVE_LEFT_DOWN);
        IBakedModel bakedModelWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(ACTIVE_RIGHT_DOWN);
        IBakedModel bakedModelEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(ACTIVE_LEFT_TOP);
        IBakedModel bakedModelNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(ACTIVE_RIGHT_TOP);
        IBakedModel bakedModelSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        return new BigFenceComposite(spriteGetter.apply(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE)),
                bakedModelCore, bakedModelDown, bakedModelUp, bakedModelWest, bakedModelEast,
                bakedModelNorth, bakedModelSouth);
    }
}
