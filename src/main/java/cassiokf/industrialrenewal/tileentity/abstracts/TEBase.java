package cassiokf.industrialrenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public abstract class TEBase extends TileEntity
{
    public static final String[] EMPTY_ARRAY = new String[0];
    public static final Random rand = new Random();

    public void onBlockBreak()
    {
    }
}
