package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.tab.IndustrialRenewalRailroadTab;
import cassiokf.industrialrenewal.tab.IndustrialRenewalTab;
import cassiokf.industrialrenewal.tab.IndustrialRenewalWIPTab;

public class References {

    public static final String MODID = "industrialrenewal";

    public static final String NAME = "Industrial Renewal";

    public static final String VERSION = "0.15.0";

    public static final String NETWORKCHANNEL = MODID;

    public static final IndustrialRenewalTab CREATIVE_IR_TAB = new IndustrialRenewalTab();
    public static final IndustrialRenewalRailroadTab CREATIVE_IRLOCOMOTIVE_TAB = new IndustrialRenewalRailroadTab();
    public static final IndustrialRenewalWIPTab CREAATIVE_IRWIP_TAB = new IndustrialRenewalWIPTab();

    public static final String GUI_FACTORY = "cassiokf.industrialrenewal.config.IRConfigGuiFactory";

    public static final int ENTITY_CARGOCONTAINER_ID = 120;
    public static final int ENTITY_STEAMLOCOMOTIVE_ID = 121;
    public static final int ENTITY_FLUIDCONTAINER_ID = 122;
    public static final int ENTITY_LOGCART_ID = 123;
    public static final int ENTITY_PASSENGERCAR_ID = 124;
    public static final int ENTITY_FLATCART_ID = 125;
    public static final int ENTITY_HOPPERCART_ID = 126;

    public static final String VERSION_CHECKER_URL = "https://raw.githubusercontent.com/Cassiobsk8/Industrial_Renewal/master/update.json";
}
