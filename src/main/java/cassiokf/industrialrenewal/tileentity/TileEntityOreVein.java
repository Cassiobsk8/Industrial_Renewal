package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.enums.OreQuality;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityOreVein extends TEBase
{
    private final int oreQuantity;

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

