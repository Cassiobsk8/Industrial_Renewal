package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.PillarBaseComposite;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Function;

public class FloorFluidPipeIModel extends BaseModelGeometry
{
    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("industrialrenewal:pipe_fluid/pipe_core");
    public static final ModelResourceLocation MODEL_MASTER = new ModelResourceLocation("industrialrenewal:pipe_energy/cable_master");

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

    public static final ModelResourceLocation MODEL3_DOWN = new ModelResourceLocation("industrialrenewal:indfloor/con_down");
    public static final ModelResourceLocation MODEL3_UP = new ModelResourceLocation("industrialrenewal:indfloor/con_up");
    public static final ModelResourceLocation MODEL3_NORTH = new ModelResourceLocation("industrialrenewal:indfloor/con_north");
    public static final ModelResourceLocation MODEL3_SOUTH = new ModelResourceLocation("industrialrenewal:indfloor/con_south");
    public static final ModelResourceLocation MODEL3_WEST = new ModelResourceLocation("industrialrenewal:indfloor/con_west");
    public static final ModelResourceLocation MODEL3_EAST = new ModelResourceLocation("industrialrenewal:indfloor/con_east");

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        IUnbakedModel subComponent = ModelLoader.instance().getUnbakedModel(MODEL_CORE);
        IBakedModel bakedModelCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MASTER);
        IBakedModel bakedModelMaster = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_DOWN);
        IBakedModel bakedModelDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_UP);
        IBakedModel bakedModelUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_WEST);
        IBakedModel bakedModelWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_EAST);
        IBakedModel bakedModelEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_NORTH);
        IBakedModel bakedModelNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_SOUTH);
        IBakedModel bakedModelSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_DOWN);
        IBakedModel bakedModel2Down = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_UP);
        IBakedModel bakedModel2Up = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_WEST);
        IBakedModel bakedModel2West = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_EAST);
        IBakedModel bakedModel2East = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_NORTH);
        IBakedModel bakedModel2North = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_SOUTH);
        IBakedModel bakedModel2South = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL3_DOWN);
        IBakedModel bakedModel3Down = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL3_UP);
        IBakedModel bakedModel3Up = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL3_WEST);
        IBakedModel bakedModel3West = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL3_EAST);
        IBakedModel bakedModel3East = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL3_NORTH);
        IBakedModel bakedModel3North = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL3_SOUTH);
        IBakedModel bakedModel3South = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        return new PillarBaseComposite(bakedModelCore, bakedModelMaster, bakedModelDown, bakedModelUp, bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth,
                bakedModel2Down, bakedModel2Up, bakedModel2West, bakedModel2East, bakedModel2North, bakedModel2South,
                bakedModel3Down, bakedModel3Up, bakedModel3West, bakedModel3East, bakedModel3North, bakedModel3South);
    }
}
