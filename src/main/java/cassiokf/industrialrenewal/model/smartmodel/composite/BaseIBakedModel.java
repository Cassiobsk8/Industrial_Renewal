package cassiokf.industrialrenewal.model.smartmodel.composite;

import cassiokf.industrialrenewal.References;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class BaseIBakedModel implements IDynamicBakedModel
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "blocks/pipe");

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
    public boolean func_230044_c_()
    {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.EMPTY;
    }

    public static boolean isLinkPresent(IModelData data, ModelProperty<Boolean> whichLink)
    {
        return data.hasProperty(whichLink) && data.getData(whichLink);
    }
}
