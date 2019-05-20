package cassiokf.industrialrenewal.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.items.IItemHandler;
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

    public static boolean IsInventoryEmpty(IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsInventoryFull(IItemHandler handler) {
        int slotsFull = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                slotsFull++;
            }
        }
        return slotsFull == handler.getSlots();
    }

    public static IFluidHandler wrapFluidBlock(Block block, World world, BlockPos pos) {
        if (block instanceof IFluidBlock) {
            return new FluidBlockWrapper((IFluidBlock) block, world, pos);
        } else if (block instanceof BlockLiquid) {
            return new BlockLiquidWrapper((BlockLiquid) block, world, pos);
        } else {
            return new BlockWrapper(block, world, pos);
        }
    }
}
