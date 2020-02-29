package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.RoofComposite;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class RoofModel extends BaseModelGeometry
{
    public static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("industrialrenewal:blocks/anvil_base");

    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:roof_subblocks/roof_core");
    public static final ModelResourceLocation MODEL_DOWN = new ModelResourceLocation("industrialrenewal:roof_subblocks/roof_down");
    public static final ModelResourceLocation MODEL_NORTH = new ModelResourceLocation("industrialrenewal:roof_subblocks/roof_north");
    public static final ModelResourceLocation MODEL_SOUTH = new ModelResourceLocation("industrialrenewal:roof_subblocks/roof_south");
    public static final ModelResourceLocation MODEL_WEST = new ModelResourceLocation("industrialrenewal:roof_subblocks/roof_west");
    public static final ModelResourceLocation MODEL_EAST = new ModelResourceLocation("industrialrenewal:roof_subblocks/roof_east");

    // return all the textures used by this model (not strictly needed for this example because we load all the subcomponent
    //   models during the bake anyway)
    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return Collections.singletonList(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_SHEET));
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        IUnbakedModel subComponent = ModelLoader.instance().getUnbakedModel(MODEL_CORE);
        IBakedModel bakedModelCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_DOWN);
        IBakedModel bakedModelDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_WEST);
        IBakedModel bakedModelWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_EAST);
        IBakedModel bakedModelEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_NORTH);
        IBakedModel bakedModelNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_SOUTH);
        IBakedModel bakedModelSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        return new RoofComposite(bakedModelCore, bakedModelDown, bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth);
    }
}
