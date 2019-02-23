package cassiokf.industrialrenewal.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void sendChatMessage(String str) {
        Minecraft.getMinecraft().player.sendChatMessage(str);
    }

    public static void sendConsoleMessage(String str) {
        System.out.println(str);
    }

    public static boolean isWood(ItemStack stack) {
        int[] array = OreDictionary.getOreIDs(stack);
        int size = array.length;
        List<Integer> oreList = new ArrayList<>();
        oreList.add(OreDictionary.getOreID("logWood"));
        oreList.add(OreDictionary.getOreID("logRubber"));
        boolean isLog = false;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (oreList.contains(array[i])) {
                    isLog = true;
                    break;
                }
            }
        }
        return isLog;
    }
}
