package cassiokf.industrialrenewal.util;

import net.minecraft.client.Minecraft;

public class Utils {

    public static void sendChatMessage(String str) {
        Minecraft.getMinecraft().player.sendChatMessage(str);
    }

    public static void sendConsoleMessage(String str) {
        System.out.println(str);
    }
}
