package cassiokf.industrialrenewal.model.smartmodel.composite;

import cassiokf.industrialrenewal.tileentity.abstracts.TE6WayConnection;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseCoreSidesComposite extends BaseIBakedModel
{
    private IBakedModel modelCore;
    private IBakedModel modelDown;
    private IBakedModel modelUp;
    private IBakedModel modelWest;
    private IBakedModel modelEast;
    private IBakedModel modelNorth;
    private IBakedModel modelSouth;

    private TextureAtlasSprite sprite;

    public BaseCoreSidesComposite(TextureAtlasSprite sprite,
                                  IBakedModel i_modelCore, IBakedModel i_modelDown, IBakedModel i_modelUp, IBakedModel i_modelWest,
                                  IBakedModel i_modelEast, IBakedModel i_modelNorth, IBakedModel i_modelSouth)
    {
        modelCore = i_modelCore;
        modelDown = i_modelDown;
        modelUp = i_modelUp;
        modelWest = i_modelWest;
        modelEast = i_modelEast;
        modelNorth = i_modelNorth;
        modelSouth = i_modelSouth;
        this.sprite = sprite;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
    {
        if (side != null) return new ArrayList<>();
        List<BakedQuad> quadsList = new ArrayList<>(modelCore.getQuads(state, side, rand, extraData));

        if (isLinkPresent(extraData, TE6WayConnection.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TE6WayConnection.UP))
        {
            quadsList.addAll(modelUp.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TE6WayConnection.WEST))
        {
            quadsList.addAll(modelWest.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TE6WayConnection.EAST))
        {
            quadsList.addAll(modelEast.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TE6WayConnection.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(state, side, rand, extraData));
        }
        if (isLinkPresent(extraData, TE6WayConnection.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(state, side, rand, extraData));
        }
        return quadsList;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return sprite;
    }
}
