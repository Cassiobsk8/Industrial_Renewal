package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.util.GUIHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static SimpleNetworkWrapper INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.NETWORKCHANNEL);
        NetworkRegistry.INSTANCE.registerGuiHandler(IndustrialRenewal.instance, new GUIHandler());

        int dis = 0;

        INSTANCE.registerMessage(new PacketFirstAidKit.Handler(), PacketFirstAidKit.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnFirstAidKit.Handler(), PacketReturnFirstAidKit.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketSteamLocomotive.Handler(), PacketSteamLocomotive.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnSteamLocomotive.Handler(), PacketReturnSteamLocomotive.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketRecordPlayer.Handler(), PacketRecordPlayer.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnRecordPlayer.Handler(), PacketReturnRecordPlayer.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketCargoLoader.Handler(), PacketCargoLoader.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnCargoLoader.Handler(), PacketReturnCargoLoader.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketEntityDetector.Handler(), PacketEntityDetector.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnEntityDetector.Handler(), PacketReturnEntityDetector.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketReturnFuseBox.Handler(), PacketReturnFuseBox.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketLogCart.Handler(), PacketLogCart.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnLogCart.Handler(), PacketReturnLogCart.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketFluidBase.Handler(), PacketFluidBase.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnFluidBase.Handler(), PacketReturnFluidBase.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketFluidLoader.Handler(), PacketFluidLoader.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnFluidLoader.Handler(), PacketReturnFluidLoader.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketBarrel.Handler(), PacketBarrel.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnBarrel.Handler(), PacketReturnBarrel.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketBatteryBank.Handler(), PacketBatteryBank.class, dis++, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnBatteryBank.Handler(), PacketReturnBatteryBank.class, dis++, Side.SERVER);
    }
}
