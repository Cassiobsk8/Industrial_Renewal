package cassiokf.industrialrenewal.util.compat.jei.lathe;

import cassiokf.industrialrenewal.References;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractLatheRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/container/lathe.png");
    protected static final int input = 0;
    protected static final int output = 1;
    protected final IDrawableStatic cutterStatic;
    protected final IDrawable icon;

    protected AbstractLatheRecipeCategory(IGuiHelper helper)
    {
        cutterStatic = helper.createDrawable(TEXTURE, 176, 70, 7, 13);
        icon = helper.createDrawable(TEXTURE, 240, 0, 16, 16);
    }
}
