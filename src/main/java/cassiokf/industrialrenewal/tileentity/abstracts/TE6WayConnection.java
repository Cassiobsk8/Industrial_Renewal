package cassiokf.industrialrenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class TE6WayConnection extends TileEntitySyncable
{
    public static final ModelProperty<Boolean> UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST = new ModelProperty<>();

    public TE6WayConnection(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }
}
