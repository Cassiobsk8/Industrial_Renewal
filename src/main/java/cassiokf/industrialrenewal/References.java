package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.group.IndRItemGroup;
import cassiokf.industrialrenewal.group.IndRRailroadItemGroup;
import cassiokf.industrialrenewal.group.IndRWIPItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fluids.FluidAttributes;

public class References
{

    public static final String MODID = "industrialrenewal";

    public static final String NAME = "Industrial Renewal";

    public static final String NETWORKCHANNEL = MODID;

    public static final ItemGroup GROUP_INDR = new IndRItemGroup();
    public static final ItemGroup GROUP_INDR_RAILROAD = new IndRRailroadItemGroup();
    public static final ItemGroup GROUP_INDR_WIP = new IndRWIPItemGroup();

    public static final int BUCKET_VOLUME = FluidAttributes.BUCKET_VOLUME;

    public static final int ENTITY_CARGOCONTAINER_ID = 120;
    public static final int ENTITY_STEAMLOCOMOTIVE_ID = 121;
    public static final int ENTITY_FLUIDCONTAINER_ID = 122;
    public static final int ENTITY_LOGCART_ID = 123;
    public static final int ENTITY_PASSENGERCAR_ID = 124;
    public static final int ENTITY_FLATCART_ID = 125;
    public static final int ENTITY_HOPPERCART_ID = 126;
}
