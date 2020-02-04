package cassiokf.industrialrenewal.model.smartmodel.composite;

import cassiokf.industrialrenewal.blocks.pipes.BlockCableTray;
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

public class CableTrayComposite implements IBakedModel
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
        if (isLinkPresent(extendedBlockState, BlockPipeBase.WEST))
        {
            quadsList.addAll(modelWest.getQuads(extendedBlockState, side, rand));
        } else
        {
            quadsList.addAll(model2West.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.EAST))
        {
            quadsList.addAll(modelEast.getQuads(extendedBlockState, side, rand));
        } else
        {
            quadsList.addAll(model2East.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.NORTH))
        {
            quadsList.addAll(modelNorth.getQuads(extendedBlockState, side, rand));
        } else
        {
            quadsList.addAll(model2North.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.SOUTH))
        {
            quadsList.addAll(modelSouth.getQuads(extendedBlockState, side, rand));
        } else
        {
            quadsList.addAll(model2South.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.UP))
        {
            quadsList.addAll(modelUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockPipeBase.DOWN))
        {
            quadsList.addAll(modelDown.getQuads(extendedBlockState, side, rand));
        }
        // PIPES
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_CORE))
        {
            quadsList.addAll(modelPipeCore.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_SOUTH))
        {
            quadsList.addAll(modelPipeSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_NORTH))
        {
            quadsList.addAll(modelPipeNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_EAST))
        {
            quadsList.addAll(modelPipeEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_WEST))
        {
            quadsList.addAll(modelPipeWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_UP))
        {
            quadsList.addAll(modelPipeUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE_DOWN))
        {
            quadsList.addAll(modelPipeDown.getQuads(extendedBlockState, side, rand));
        }

        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE2_NORTH))
        {
            quadsList.addAll(model2PipeNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE2_SOUTH))
        {
            quadsList.addAll(model2PipeSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE2_EAST))
        {
            quadsList.addAll(model2PipeEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE2_WEST))
        {
            quadsList.addAll(model2PipeWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE2_UP))
        {
            quadsList.addAll(model2PipeUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.PIPE2_DOWN))
        {
            quadsList.addAll(model2PipeDown.getQuads(extendedBlockState, side, rand));
        }

        // HV
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_CORE))
        {
            quadsList.addAll(modelHvCore.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_SOUTH))
        {
            quadsList.addAll(modelHvSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_NORTH))
        {
            quadsList.addAll(modelHvNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_EAST))
        {
            quadsList.addAll(modelHvEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_WEST))
        {
            quadsList.addAll(modelHvWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_UP))
        {
            quadsList.addAll(modelHvUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV_DOWN))
        {
            quadsList.addAll(modelHvDown.getQuads(extendedBlockState, side, rand));
        }

        if (isLinkPresent(extendedBlockState, BlockCableTray.HV2_NORTH))
        {
            quadsList.addAll(model2HvNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV2_SOUTH))
        {
            quadsList.addAll(model2HvSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV2_EAST))
        {
            quadsList.addAll(model2HvEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV2_WEST))
        {
            quadsList.addAll(model2HvWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV2_UP))
        {
            quadsList.addAll(model2HvUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.HV2_DOWN))
        {
            quadsList.addAll(model2HvDown.getQuads(extendedBlockState, side, rand));
        }

        // MV
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_CORE))
        {
            quadsList.addAll(modelMvCore.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_SOUTH))
        {
            quadsList.addAll(modelMvSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_NORTH))
        {
            quadsList.addAll(modelMvNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_EAST))
        {
            quadsList.addAll(modelMvEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_WEST))
        {
            quadsList.addAll(modelMvWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_UP))
        {
            quadsList.addAll(modelMvUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV_DOWN))
        {
            quadsList.addAll(modelMvDown.getQuads(extendedBlockState, side, rand));
        }

        if (isLinkPresent(extendedBlockState, BlockCableTray.MV2_NORTH))
        {
            quadsList.addAll(model2MvNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV2_SOUTH))
        {
            quadsList.addAll(model2MvSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV2_EAST))
        {
            quadsList.addAll(model2MvEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV2_WEST))
        {
            quadsList.addAll(model2MvWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV2_UP))
        {
            quadsList.addAll(model2MvUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.MV2_DOWN))
        {
            quadsList.addAll(model2MvDown.getQuads(extendedBlockState, side, rand));
        }

        // LV
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_CORE))
        {
            quadsList.addAll(modelLvCore.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_SOUTH))
        {
            quadsList.addAll(modelLvSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_NORTH))
        {
            quadsList.addAll(modelLvNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_EAST))
        {
            quadsList.addAll(modelLvEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_WEST))
        {
            quadsList.addAll(modelLvWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_UP))
        {
            quadsList.addAll(modelLvUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV_DOWN))
        {
            quadsList.addAll(modelLvDown.getQuads(extendedBlockState, side, rand));
        }

        if (isLinkPresent(extendedBlockState, BlockCableTray.LV2_NORTH))
        {
            quadsList.addAll(model2LvNorth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV2_SOUTH))
        {
            quadsList.addAll(model2LvSouth.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV2_EAST))
        {
            quadsList.addAll(model2LvEast.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV2_WEST))
        {
            quadsList.addAll(model2LvWest.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV2_UP))
        {
            quadsList.addAll(model2LvUp.getQuads(extendedBlockState, side, rand));
        }
        if (isLinkPresent(extendedBlockState, BlockCableTray.LV2_DOWN))
        {
            quadsList.addAll(model2LvDown.getQuads(extendedBlockState, side, rand));
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
