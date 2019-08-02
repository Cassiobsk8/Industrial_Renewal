package cassiokf.industrialrenewal.model.smartmodel;

import cassiokf.industrialrenewal.blocks.BlockRoof;
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

public class RoofComposite implements IBakedModel
{
    private IBakedModel modelCore;
    private IBakedModel modelDown;
    private IBakedModel modelWest;
    private IBakedModel modelEast;
    private IBakedModel modelNorth;
    private IBakedModel modelSouth;

    public RoofComposite(IBakedModel i_modelCore, IBakedModel i_modelDown, IBakedModel i_modelWest, IBakedModel i_modelEast,
                         IBakedModel i_modelNorth, IBakedModel i_modelSouth)
    {
        modelCore = i_modelCore;
        modelDown = i_modelDown;
        modelWest = i_modelWest;
        modelEast = i_modelEast;
        modelNorth = i_modelNorth;
        modelSouth = i_modelSouth;
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
        if (isLinkPresent(extendedBlockState, BlockRoof.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockRoof.WEST))
        {
            quadsList.addAll(modelWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockRoof.EAST))
        {
            quadsList.addAll(modelEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockRoof.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockRoof.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(extendedBlockState, side, rand));
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
