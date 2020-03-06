package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.BaseCoreSidesComposite;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Function;

public class BatteryBankIModel extends BaseModelGeometry
{
    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:energy_connector/battery_bank_core");
    public static final ModelResourceLocation MODEL_UP = new ModelResourceLocation("industrialrenewal:energy_connector/con_out_up");
    public static final ModelResourceLocation MODEL_DOWN = new ModelResourceLocation("industrialrenewal:energy_connector/con_out_down");
    public static final ModelResourceLocation MODEL_NORTH = new ModelResourceLocation("industrialrenewal:energy_connector/con_out_north");
    public static final ModelResourceLocation MODEL_SOUTH = new ModelResourceLocation("industrialrenewal:energy_connector/con_out_south");
    public static final ModelResourceLocation MODEL_WEST = new ModelResourceLocation("industrialrenewal:energy_connector/con_out_west");
    public static final ModelResourceLocation MODEL_EAST = new ModelResourceLocation("industrialrenewal:energy_connector/con_out_east");

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        IUnbakedModel subComponent = ModelLoader.instance().getUnbakedModel(MODEL_CORE);
        IBakedModel bakedModelCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);
        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_UP);
        IBakedModel bakedModelUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);
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

        return new BaseCoreSidesComposite(spriteGetter.apply(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE)),
                bakedModelCore,
                bakedModelDown,
                bakedModelUp,
                bakedModelWest,
                bakedModelEast,
                bakedModelNorth,
                bakedModelSouth);
    }
}
