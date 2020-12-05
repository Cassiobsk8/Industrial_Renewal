package cassiokf.industrialrenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Random;

public abstract class TEBase extends TileEntity
{
    public static final String[] EMPTY_ARRAY = new String[0];
    public static final Random rand = new Random();

    public TEBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public void onBlockBreak()
    {
    }
}
