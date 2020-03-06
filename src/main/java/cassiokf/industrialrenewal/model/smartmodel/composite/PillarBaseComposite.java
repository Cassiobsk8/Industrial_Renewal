package cassiokf.industrialrenewal.model.smartmodel.composite;


import cassiokf.industrialrenewal.tileentity.abstracts.TETubeBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
    private TextureAtlasSprite sprite;

    public PillarBaseComposite(TextureAtlasSprite sprite,
                               IBakedModel i_modelCore, IBakedModel i_modelMaster, IBakedModel i_modelDown,
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
        this.sprite = sprite;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
    {
        List<BakedQuad> quadsList = new LinkedList<BakedQuad>(modelCore.getQuads(state, side, rand, extraData));

        if (isLinkPresent(extraData, TETubeBase.MASTER))
        {
            quadsList.addAll(modelMaster.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.UP))
        {
            quadsList.addAll(modelUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WEST))
        {
            quadsList.addAll(modelWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.EAST))
        {
            quadsList.addAll(modelEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(state, side, rand, extraData));
        }

        if (isLinkPresent(extraData, TETubeBase.CDOWN))
        {
            quadsList.addAll(model2Down.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.CUP))
        {
            quadsList.addAll(model2Up.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.CWEST))
        {
            quadsList.addAll(model2West.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.CEAST))
        {
            quadsList.addAll(model2East.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.CNORTH))
        {
            quadsList.addAll(model2North.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.CSOUTH))
        {
            quadsList.addAll(model2South.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WDOWN))
        {
            quadsList.addAll(model3Down.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WUP))
        {
            quadsList.addAll(model3Up.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WWEST))
        {
            quadsList.addAll(model3West.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WEAST))
        {
            quadsList.addAll(model3East.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WNORTH))
        {
            quadsList.addAll(model3North.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TETubeBase.WSOUTH))
        {
            quadsList.addAll(model3South.getQuads(state, side, rand, extraData));
        }
        return quadsList;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return sprite;
    }
}
