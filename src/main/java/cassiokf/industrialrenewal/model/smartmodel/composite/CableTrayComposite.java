package cassiokf.industrialrenewal.model.smartmodel.composite;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CableTrayComposite extends BaseIBakedModel
{
    private IBakedModel modelCore;
    private IBakedModel modelMaster;

    //Tray connections
    private IBakedModel modelWest;
    private IBakedModel modelEast;
    private IBakedModel modelNorth;
    private IBakedModel modelSouth;
    private IBakedModel modelUp;
    private IBakedModel modelDown;

    //close tray
    private IBakedModel model2West;
    private IBakedModel model2East;
    private IBakedModel model2North;
    private IBakedModel model2South;

    //between trays
    private IBakedModel modelPipeCore;
    private IBakedModel modelPipeNorth;
    private IBakedModel modelPipeSouth;
    private IBakedModel modelPipeEast;
    private IBakedModel modelPipeWest;
    private IBakedModel modelPipeUp;
    private IBakedModel modelPipeDown;

    private IBakedModel modelHvCore;
    private IBakedModel modelHvNorth;
    private IBakedModel modelHvSouth;
    private IBakedModel modelHvEast;
    private IBakedModel modelHvWest;
    private IBakedModel modelHvUp;
    private IBakedModel modelHvDown;

    private IBakedModel modelMvCore;
    private IBakedModel modelMvNorth;
    private IBakedModel modelMvSouth;
    private IBakedModel modelMvEast;
    private IBakedModel modelMvWest;
    private IBakedModel modelMvUp;
    private IBakedModel modelMvDown;

    private IBakedModel modelLvCore;
    private IBakedModel modelLvNorth;
    private IBakedModel modelLvSouth;
    private IBakedModel modelLvEast;
    private IBakedModel modelLvWest;
    private IBakedModel modelLvUp;
    private IBakedModel modelLvDown;

    //to capability
    private IBakedModel model2PipeNorth;
    private IBakedModel model2PipeSouth;
    private IBakedModel model2PipeEast;
    private IBakedModel model2PipeWest;
    private IBakedModel model2PipeUp;
    private IBakedModel model2PipeDown;

    private IBakedModel model2HvNorth;
    private IBakedModel model2HvSouth;
    private IBakedModel model2HvEast;
    private IBakedModel model2HvWest;
    private IBakedModel model2HvUp;
    private IBakedModel model2HvDown;

    private IBakedModel model2MvNorth;
    private IBakedModel model2MvSouth;
    private IBakedModel model2MvEast;
    private IBakedModel model2MvWest;
    private IBakedModel model2MvUp;
    private IBakedModel model2MvDown;

    private IBakedModel model2LvNorth;
    private IBakedModel model2LvSouth;
    private IBakedModel model2LvEast;
    private IBakedModel model2LvWest;
    private IBakedModel model2LvUp;
    private IBakedModel model2LvDown;

    public CableTrayComposite(IBakedModel i_modelCore, IBakedModel i_modelMaster, IBakedModel i_modelWest,
                              IBakedModel i_modelEast, IBakedModel i_modelNorth, IBakedModel i_modelSouth,
                              IBakedModel i_modelUp, IBakedModel i_modelDown, IBakedModel i_model2West,
                              IBakedModel i_model2East, IBakedModel i_model2North, IBakedModel i_model2South,
                              IBakedModel i_modelPipeCore, IBakedModel i_modelPipeNorth, IBakedModel i_modelPipeSouth,
                              IBakedModel i_modelPipeEast, IBakedModel i_modelPipeWest, IBakedModel i_modelPipeUp,
                              IBakedModel i_modelPipeDown, IBakedModel i_model2PipeNorth,
                              IBakedModel i_model2PipeSouth, IBakedModel i_model2PipeEast, IBakedModel i_model2PipeWest,
                              IBakedModel i_model2PipeUp, IBakedModel i_model2PipeDown,
                              IBakedModel i_modelHvCore, IBakedModel i_modelHvNorth, IBakedModel i_modelHvSouth,
                              IBakedModel i_modelHvEast, IBakedModel i_modelHvWest, IBakedModel i_modelHvUp,
                              IBakedModel i_modelHvDown, IBakedModel i_model2HvNorth,
                              IBakedModel i_model2HvSouth, IBakedModel i_model2HvEast, IBakedModel i_model2HvWest,
                              IBakedModel i_model2HvUp, IBakedModel i_model2HvDown,
                              IBakedModel i_modelMvCore, IBakedModel i_modelMvNorth, IBakedModel i_modelMvSouth,
                              IBakedModel i_modelMvEast, IBakedModel i_modelMvWest, IBakedModel i_modelMvUp,
                              IBakedModel i_modelMvDown, IBakedModel i_model2MvNorth,
                              IBakedModel i_model2MvSouth, IBakedModel i_model2MvEast, IBakedModel i_model2MvWest,
                              IBakedModel i_model2MvUp, IBakedModel i_model2MvDown,
                              IBakedModel i_modelLvCore, IBakedModel i_modelLvNorth, IBakedModel i_modelLvSouth,
                              IBakedModel i_modelLvEast, IBakedModel i_modelLvWest, IBakedModel i_modelLvUp,
                              IBakedModel i_modelLvDown, IBakedModel i_model2LvNorth,
                              IBakedModel i_model2LvSouth, IBakedModel i_model2LvEast, IBakedModel i_model2LvWest,
                              IBakedModel i_model2LvUp, IBakedModel i_model2LvDown)
    {
        modelCore = i_modelCore;
        modelMaster = i_modelMaster;
        modelWest = i_modelWest;
        modelEast = i_modelEast;
        modelNorth = i_modelNorth;
        modelSouth = i_modelSouth;
        modelUp = i_modelUp;
        modelDown = i_modelDown;

        model2West = i_model2West;
        model2East = i_model2East;
        model2North = i_model2North;
        model2South = i_model2South;

        modelPipeCore = i_modelPipeCore;
        modelPipeNorth = i_modelPipeNorth;
        modelPipeSouth = i_modelPipeSouth;
        modelPipeEast = i_modelPipeEast;
        modelPipeWest = i_modelPipeWest;
        modelPipeUp = i_modelPipeUp;
        modelPipeDown = i_modelPipeDown;

        model2PipeNorth = i_model2PipeNorth;
        model2PipeSouth = i_model2PipeSouth;
        model2PipeEast = i_model2PipeEast;
        model2PipeWest = i_model2PipeWest;
        model2PipeUp = i_model2PipeUp;
        model2PipeDown = i_model2PipeDown;

        modelHvCore = i_modelHvCore;
        modelHvNorth = i_modelHvNorth;
        modelHvSouth = i_modelHvSouth;
        modelHvEast = i_modelHvEast;
        modelHvWest = i_modelHvWest;
        modelHvUp = i_modelHvUp;
        modelHvDown = i_modelHvDown;

        model2HvNorth = i_model2HvNorth;
        model2HvSouth = i_model2HvSouth;
        model2HvEast = i_model2HvEast;
        model2HvWest = i_model2HvWest;
        model2HvUp = i_model2HvUp;
        model2HvDown = i_model2HvDown;

        modelMvCore = i_modelMvCore;
        modelMvNorth = i_modelMvNorth;
        modelMvSouth = i_modelMvSouth;
        modelMvEast = i_modelMvEast;
        modelMvWest = i_modelMvWest;
        modelMvUp = i_modelMvUp;
        modelMvDown = i_modelMvDown;

        model2MvNorth = i_model2MvNorth;
        model2MvSouth = i_model2MvSouth;
        model2MvEast = i_model2MvEast;
        model2MvWest = i_model2MvWest;
        model2MvUp = i_model2MvUp;
        model2MvDown = i_model2MvDown;

        modelLvCore = i_modelLvCore;
        modelLvNorth = i_modelLvNorth;
        modelLvSouth = i_modelLvSouth;
        modelLvEast = i_modelLvEast;
        modelLvWest = i_modelLvWest;
        modelLvUp = i_modelLvUp;
        modelLvDown = i_modelLvDown;

        model2LvNorth = i_model2LvNorth;
        model2LvSouth = i_model2LvSouth;
        model2LvEast = i_model2LvEast;
        model2LvWest = i_model2LvWest;
        model2LvUp = i_model2LvUp;
        model2LvDown = i_model2LvDown;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
    {
        List<BakedQuad> quadsList = new LinkedList<BakedQuad>(modelCore.getQuads(state, side, rand, extraData));

        if (isLinkPresent(extraData, TileEntityCableTray.MASTER))
        {
            quadsList.addAll(modelMaster.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.WEST))
        {
            quadsList.addAll(modelWest.getQuads(state, side, rand, extraData));
        } else
        {
            quadsList.addAll(model2West.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.EAST))
        {
            quadsList.addAll(modelEast.getQuads(state, side, rand, extraData));
        } else
        {
            quadsList.addAll(model2East.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(state, side, rand, extraData));
        } else
        {
            quadsList.addAll(model2North.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(state, side, rand, extraData));
        } else
        {
            quadsList.addAll(model2South.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.UP))
        {
            quadsList.addAll(modelUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(state, side, rand, extraData));
        }
        // PIPES
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_CORE))
        {
            quadsList.addAll(modelPipeCore.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_SOUTH))
        {
            quadsList.addAll(modelPipeSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_NORTH))
        {
            quadsList.addAll(modelPipeNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_EAST))
        {
            quadsList.addAll(modelPipeEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_WEST))
        {
            quadsList.addAll(modelPipeWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_UP))
        {
            quadsList.addAll(modelPipeUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE_DOWN))
        {
            quadsList.addAll(modelPipeDown.getQuads(state, side, rand, extraData));
        }

        if (isLinkPresent(extraData, TileEntityCableTray.PIPE2_NORTH))
        {
            quadsList.addAll(model2PipeNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE2_SOUTH))
        {
            quadsList.addAll(model2PipeSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE2_EAST))
        {
            quadsList.addAll(model2PipeEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE2_WEST))
        {
            quadsList.addAll(model2PipeWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE2_UP))
        {
            quadsList.addAll(model2PipeUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.PIPE2_DOWN))
        {
            quadsList.addAll(model2PipeDown.getQuads(state, side, rand, extraData));
        }

        // HV
        if (isLinkPresent(extraData, TileEntityCableTray.HV_CORE))
        {
            quadsList.addAll(modelHvCore.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV_SOUTH))
        {
            quadsList.addAll(modelHvSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV_NORTH))
        {
            quadsList.addAll(modelHvNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV_EAST))
        {
            quadsList.addAll(modelHvEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV_WEST))
        {
            quadsList.addAll(modelHvWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV_UP))
        {
            quadsList.addAll(modelHvUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV_DOWN))
        {
            quadsList.addAll(modelHvDown.getQuads(state, side, rand, extraData));
        }

        if (isLinkPresent(extraData, TileEntityCableTray.HV2_NORTH))
        {
            quadsList.addAll(model2HvNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV2_SOUTH))
        {
            quadsList.addAll(model2HvSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV2_EAST))
        {
            quadsList.addAll(model2HvEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV2_WEST))
        {
            quadsList.addAll(model2HvWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV2_UP))
        {
            quadsList.addAll(model2HvUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.HV2_DOWN))
        {
            quadsList.addAll(model2HvDown.getQuads(state, side, rand, extraData));
        }

        // MV
        if (isLinkPresent(extraData, TileEntityCableTray.MV_CORE))
        {
            quadsList.addAll(modelMvCore.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV_SOUTH))
        {
            quadsList.addAll(modelMvSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV_NORTH))
        {
            quadsList.addAll(modelMvNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV_EAST))
        {
            quadsList.addAll(modelMvEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV_WEST))
        {
            quadsList.addAll(modelMvWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV_UP))
        {
            quadsList.addAll(modelMvUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV_DOWN))
        {
            quadsList.addAll(modelMvDown.getQuads(state, side, rand, extraData));
        }

        if (isLinkPresent(extraData, TileEntityCableTray.MV2_NORTH))
        {
            quadsList.addAll(model2MvNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV2_SOUTH))
        {
            quadsList.addAll(model2MvSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV2_EAST))
        {
            quadsList.addAll(model2MvEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV2_WEST))
        {
            quadsList.addAll(model2MvWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV2_UP))
        {
            quadsList.addAll(model2MvUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.MV2_DOWN))
        {
            quadsList.addAll(model2MvDown.getQuads(state, side, rand, extraData));
        }

        // LV
        if (isLinkPresent(extraData, TileEntityCableTray.LV_CORE))
        {
            quadsList.addAll(modelLvCore.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV_SOUTH))
        {
            quadsList.addAll(modelLvSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV_NORTH))
        {
            quadsList.addAll(modelLvNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV_EAST))
        {
            quadsList.addAll(modelLvEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV_WEST))
        {
            quadsList.addAll(modelLvWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV_UP))
        {
            quadsList.addAll(modelLvUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV_DOWN))
        {
            quadsList.addAll(modelLvDown.getQuads(state, side, rand, extraData));
        }

        if (isLinkPresent(extraData, TileEntityCableTray.LV2_NORTH))
        {
            quadsList.addAll(model2LvNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV2_SOUTH))
        {
            quadsList.addAll(model2LvSouth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV2_EAST))
        {
            quadsList.addAll(model2LvEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV2_WEST))
        {
            quadsList.addAll(model2LvWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV2_UP))
        {
            quadsList.addAll(model2LvUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TileEntityCableTray.LV2_DOWN))
        {
            quadsList.addAll(model2LvDown.getQuads(state, side, rand, extraData));
        }

        return quadsList;
    }
}
