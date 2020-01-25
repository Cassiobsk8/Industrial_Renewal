package cassiokf.industrialrenewal.model.smartmodel.composite;

import cassiokf.industrialrenewal.blocks.pipes.BlockPipeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class PillarBaseComposite implements IBakedModel
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

    /**
     * Compile a list of quads for rendering.  This is done by making a list of all the quads from the component
     * models, depending on which links are present.
     * For example
     *
     * @param blockState
     * @param side       which side of the block is being rendered; null =
     * @param rand
     * @return
     */
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand)
    {
        List<BakedQuad> quadsList = new LinkedList<BakedQuad>();
        quadsList.addAll(modelCore.getQuads(blockState, side, rand));
        if (!(blockState instanceof IExtendedBlockState))
        {
            return quadsList;
        }
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) blockState;
        if (isLinkPresent(extendedBlockState, BlockPipeBase.MASTER))
        {
            quadsList.addAll(modelMaster.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.UP))
        {
            quadsList.addAll(modelUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WEST))
        {
            quadsList.addAll(modelWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.EAST))
        {
            quadsList.addAll(modelEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(extendedBlockState, side, rand));
        }

        if (isLinkPresent(extendedBlockState, BlockPipeBase.CDOWN))
        {
            quadsList.addAll(model2Down.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.CUP))
        {
            quadsList.addAll(model2Up.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.CWEST))
        {
            quadsList.addAll(model2West.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.CEAST))
        {
            quadsList.addAll(model2East.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.CNORTH))
        {
            quadsList.addAll(model2North.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.CSOUTH))
        {
            quadsList.addAll(model2South.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WDOWN))
        {
            quadsList.addAll(model3Down.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WUP))
        {
            quadsList.addAll(model3Up.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WWEST))
        {
            quadsList.addAll(model3West.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WEAST))
        {
            quadsList.addAll(model3East.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WNORTH))
        {
            quadsList.addAll(model3North.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WSOUTH))
        {
            quadsList.addAll(model3South.getQuads(extendedBlockState, side, rand));
        }
        return quadsList;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }


    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    // used for block breaking shards
    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite("industrialrenewal:blocks/pipe");

        return textureAtlasSprite;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }

    private boolean isLinkPresent(IExtendedBlockState iExtendedBlockState, IUnlistedProperty<Boolean> whichLink)
    {
        Boolean link = iExtendedBlockState.getValue(whichLink);
        if (link == null)
        {
            return false;
        }
        return link;
    }
}
