package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.model.smartmodel.composite.CableTrayComposite;
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

public class CableTrayIModel extends BaseModelGeometry
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
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        IUnbakedModel subComponent = ModelLoader.instance().getUnbakedModel(MODEL_CORE);
        IBakedModel bakedModelCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MASTER);
        IBakedModel bakedModelMaster = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_WEST);
        IBakedModel bakedModelWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_EAST);
        IBakedModel bakedModelEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_NORTH);
        IBakedModel bakedModelNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_SOUTH);
        IBakedModel bakedModelSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_DOWN);
        IBakedModel bakedModelDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_UP);
        IBakedModel bakedModelUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_WEST);
        IBakedModel bakedModel2West = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_EAST);
        IBakedModel bakedModel2East = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_NORTH);
        IBakedModel bakedModel2North = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_SOUTH);
        IBakedModel bakedModel2South = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_CORE);
        IBakedModel bakedModelPipeCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_SOUTH);
        IBakedModel bakedModelPipeSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_NORTH);
        IBakedModel bakedModelPipeNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_EAST);
        IBakedModel bakedModelPipeEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_WEST);
        IBakedModel bakedModelPipeWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_UP);
        IBakedModel bakedModelPipeUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_PIPE_DOWN);
        IBakedModel bakedModelPipeDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_PIPE_SOUTH);
        IBakedModel bakedModel2PipeSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_PIPE_NORTH);
        IBakedModel bakedModel2PipeNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_PIPE_EAST);
        IBakedModel bakedModel2PipeEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_PIPE_WEST);
        IBakedModel bakedModel2PipeWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_PIPE_UP);
        IBakedModel bakedModel2PipeUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_PIPE_DOWN);
        IBakedModel bakedModel2PipeDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        //HV

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_CORE);
        IBakedModel bakedModelHvCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_SOUTH);
        IBakedModel bakedModelHvSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_NORTH);
        IBakedModel bakedModelHvNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_EAST);
        IBakedModel bakedModelHvEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_WEST);
        IBakedModel bakedModelHvWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_UP);
        IBakedModel bakedModelHvUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_HV_DOWN);
        IBakedModel bakedModelHvDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_HV_SOUTH);
        IBakedModel bakedModel2HvSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_HV_NORTH);
        IBakedModel bakedModel2HvNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_HV_EAST);
        IBakedModel bakedModel2HvEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_HV_WEST);
        IBakedModel bakedModel2HvWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_HV_UP);
        IBakedModel bakedModel2HvUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_HV_DOWN);
        IBakedModel bakedModel2HvDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        //MV

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_CORE);
        IBakedModel bakedModelMvCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_SOUTH);
        IBakedModel bakedModelMvSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_NORTH);
        IBakedModel bakedModelMvNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_EAST);
        IBakedModel bakedModelMvEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_WEST);
        IBakedModel bakedModelMvWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_UP);
        IBakedModel bakedModelMvUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_MV_DOWN);
        IBakedModel bakedModelMvDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_MV_SOUTH);
        IBakedModel bakedModel2MvSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_MV_NORTH);
        IBakedModel bakedModel2MvNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_MV_EAST);
        IBakedModel bakedModel2MvEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_MV_WEST);
        IBakedModel bakedModel2MvWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_MV_UP);
        IBakedModel bakedModel2MvUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_MV_DOWN);
        IBakedModel bakedModel2MvDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        //LV

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_CORE);
        IBakedModel bakedModelLvCore = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_SOUTH);
        IBakedModel bakedModelLvSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_NORTH);
        IBakedModel bakedModelLvNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_EAST);
        IBakedModel bakedModelLvEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_WEST);
        IBakedModel bakedModelLvWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_UP);
        IBakedModel bakedModelLvUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL_LV_DOWN);
        IBakedModel bakedModelLvDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_LV_SOUTH);
        IBakedModel bakedModel2LvSouth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_LV_NORTH);
        IBakedModel bakedModel2LvNorth = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_LV_EAST);
        IBakedModel bakedModel2LvEast = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_LV_WEST);
        IBakedModel bakedModel2LvWest = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_LV_UP);
        IBakedModel bakedModel2LvUp = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

        subComponent = ModelLoader.instance().getUnbakedModel(MODEL2_LV_DOWN);
        IBakedModel bakedModel2LvDown = subComponent.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);

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

    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return Collections.singletonList(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_SHEET));
    }
}
