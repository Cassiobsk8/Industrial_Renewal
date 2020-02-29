package cassiokf.industrialrenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class TEBigFenceBase extends TileEntity
{
    public static final ModelProperty<Boolean> CORE = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_LEFT = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_RIGHT = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_LEFT_DOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_RIGHT_DOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_LEFT_TOP = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_RIGHT_TOP = new ModelProperty<>();

    public TEBigFenceBase(TileEntityType<?> p_i48289_1_)
    {
        super(p_i48289_1_);
    }

}
