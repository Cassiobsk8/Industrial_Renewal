package cassiokf.industrialrenewal.model.smartmodel.composite;


import cassiokf.industrialrenewal.tileentity.abstracts.TEPipesBase;
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

public class PillarBaseComposite extends BaseIBakedModel
{
    private IBakedModel modelCore;
    private IBakedModel modelMaster;
    private IBakedModel modelDown;
    private IBakedModel modelUp;
    private IBakedModel modelWest;
    private IBakedModel modelEast;
    private IBakedModel modelNorth;
    private IBakedModel modelSouth;
    private IBakedModel model2Down;
    private IBakedModel model2Up;
    private IBakedModel model2West;
    private IBakedModel model2East;
    private IBakedModel model2North;
    private IBakedModel model2South;
    private IBakedModel model3Down;
    private IBakedModel model3Up;
    private IBakedModel model3West;
    private IBakedModel model3East;
    private IBakedModel model3North;
    private IBakedModel model3South;

    public PillarBaseComposite(IBakedModel i_modelCore, IBakedModel i_modelMaster, IBakedModel i_modelDown,
                               IBakedModel i_modelUp, IBakedModel i_modelWest,
                               IBakedModel i_modelEast, IBakedModel i_modelNorth, IBakedModel i_modelSouth,
                               IBakedModel i_model2Down, IBakedModel i_model2Up, IBakedModel i_model2West,
                               IBakedModel i_model2East, IBakedModel i_model2North, IBakedModel i_model2South,
                               IBakedModel i_model3Down, IBakedModel i_model3Up, IBakedModel i_model3West,
                               IBakedModel i_model3East, IBakedModel i_model3North, IBakedModel i_model3South)
    {
        modelCore = i_modelCore;
        modelMaster = i_modelMaster;
        modelDown = i_modelDown;
        modelUp = i_modelUp;
        modelWest = i_modelWest;
        modelEast = i_modelEast;
        modelNorth = i_modelNorth;
        modelSouth = i_modelSouth;
        model2Down = i_model2Down;
        model2Up = i_model2Up;
        model2West = i_model2West;
        model2East = i_model2East;
        model2North = i_model2North;
        model2South = i_model2South;
        model3Down = i_model3Down;
        model3Up = i_model3Up;
        model3West = i_model3West;
        model3East = i_model3East;
        model3North = i_model3North;
        model3South = i_model3South;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
    {
        List<BakedQuad> quadsList = new LinkedList<BakedQuad>(modelCore.getQuads(state, side, rand, extraData));

        if (isLinkPresent(extraData, TEPipesBase.MASTER))
        {
            quadsList.addAll(modelMaster.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.UP))
        {
            quadsList.addAll(modelUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WEST))
        {
            quadsList.addAll(modelWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.EAST))
        {
            quadsList.addAll(modelEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(state, side, rand, extraData));
        }

        if (isLinkPresent(extraData, TEPipesBase.CDOWN))
        {
            quadsList.addAll(model2Down.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.CUP))
        {
            quadsList.addAll(model2Up.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.CWEST))
        {
            quadsList.addAll(model2West.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.CEAST))
        {
            quadsList.addAll(model2East.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.CNORTH))
        {
            quadsList.addAll(model2North.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.CSOUTH))
        {
            quadsList.addAll(model2South.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WDOWN))
        {
            quadsList.addAll(model3Down.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WUP))
        {
            quadsList.addAll(model3Up.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WWEST))
        {
            quadsList.addAll(model3West.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WEAST))
        {
            quadsList.addAll(model3East.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WNORTH))
        {
            quadsList.addAll(model3North.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TEPipesBase.WSOUTH))
        {
            quadsList.addAll(model3South.getQuads(state, side, rand, extraData));
        }
        return quadsList;
    }
}
