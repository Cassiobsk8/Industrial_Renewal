package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.group.IndustrialRenewalItemGroup;
import cassiokf.industrialrenewal.group.IndustrialRenewalRailroadItemGroup;
import cassiokf.industrialrenewal.group.IndustrialRenewalWIPItemGroup;
import net.minecraft.item.ItemGroup;

public class References
{

    public static final String MODID = "industrialrenewal";

    public static final String NAME = "Industrial Renewal";

    public static final String NETWORKCHANNEL = MODID;

    public static final ItemGroup CREATIVE_IR_GROUP = new IndustrialRenewalItemGroup();
    public static final ItemGroup CREATIVE_IRLOCOMOTIVE_GROUP = new IndustrialRenewalRailroadItemGroup();
    public static final ItemGroup CREAATIVE_IRWIP_GROUP = new IndustrialRenewalWIPItemGroup();

    public static final int ENTITY_CARGOCONTAINER_ID = 120;
    public static final int ENTITY_STEAMLOCOMOTIVE_ID = 121;
    public static final int ENTITY_FLUIDCONTAINER_ID = 122;
    public static final int ENTITY_LOGCART_ID = 123;
    public static final int ENTITY_PASSENGERCAR_ID = 124;
    public static final int ENTITY_FLATCART_ID = 125;
    public static final int ENTITY_HOPPERCART_ID = 126;
}
