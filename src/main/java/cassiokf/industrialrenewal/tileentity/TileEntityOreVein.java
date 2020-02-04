package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.enums.OreQuality;
import net.minecraft.tileentity.TileEntity;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityOreVein extends TileEntity
{
    private int oreQuantity;

    public TileEntityOreVein()
    {
        oreQuantity = ThreadLocalRandom.current().nextInt(100, 400 + 1);
    }

    public OreQuality getQuality()
    {
        if (oreQuantity < 200) return OreQuality.Poor;
        if (oreQuantity < 300) return OreQuality.Normal;
        return OreQuality.Pure;
    }

}

