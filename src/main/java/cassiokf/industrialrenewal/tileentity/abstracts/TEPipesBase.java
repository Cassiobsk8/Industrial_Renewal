package cassiokf.industrialrenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class TEPipesBase extends TE6WayConnection
{
    public static final ModelProperty<Boolean> MASTER = new ModelProperty<>();
    public static final ModelProperty<Boolean> CUP = new ModelProperty<>();
    public static final ModelProperty<Boolean> CDOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> CNORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> CSOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> CEAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> CWEST = new ModelProperty<>();
    //For PillarPipe
    public static final ModelProperty<Boolean> WUP = new ModelProperty<>();
    public static final ModelProperty<Boolean> WDOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> WNORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> WSOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> WWEST = new ModelProperty<>();

    public TEPipesBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }
}
