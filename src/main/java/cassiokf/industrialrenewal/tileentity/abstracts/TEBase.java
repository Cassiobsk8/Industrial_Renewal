package cassiokf.industrialrenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class TEBase extends TileEntity
{
    public static final String[] EMPTY_ARRAY = new String[0];
    public static final Random rand = new Random();

    public void onBlockBreak()
    {
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return getCapability(capability, facing) != null;
    }
}
