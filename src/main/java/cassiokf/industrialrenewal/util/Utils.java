package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.items.IItemHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils
{

    private static final Random RANDOM = new Random();

    public static void sendChatMessage(PlayerEntity player, String str)
    {
        if (player == null) Minecraft.getInstance().player.sendChatMessage(str);
        else player.sendMessage(new StringTextComponent(str));
    }

    public static void sendConsoleMessage(String str)
    {
        System.out.println(str);
    }

    public static boolean isWood(ItemStack stack)
    {/*
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
        */
        return false;
    }

    public static boolean IsInventoryEmpty(IItemHandler handler)
    {
        for (int i = 0; i < handler.getSlots(); i++)
        {
            if (!handler.getStackInSlot(i).isEmpty())
            {
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

    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory)
    {
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }

    public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack)
    {
        if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && !worldIn.restoringBlockSnapshots)
        { // do not drop items while restoring blockstates, prevents item dupe
            float f = 0.5F;
            double d0 = (double) (worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            double d1 = (double) (worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            double d2 = (double) (worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
            itementity.setDefaultPickupDelay();
            worldIn.addEntity(itementity);
        }
    }

    public static float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }

    public static double lerp(double a, double b, float f)
    {
        return a + f * (b - a);
    }

    public static double preciseLerp(double a, double b, float f)
    {
        return (1 - f) * a + f * b;
    }


    public static IFluidHandler wrapFluidBlock(BlockState state, World world, BlockPos pos)
    {
        return new BlockWrapper(state, world, pos);
    }

    public static float getInvNorm(IItemHandler inventory)
    {
        float items = 0;
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                items = items + 1;
            }
        }
        return items / (float) inventory.getSlots();
    }

    public static List<BlockPos> getBlocksIn3x3x3Centered(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = -1; y < 2; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    list.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x2x3CenteredPlus1OnTop(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = -1; y < 2; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    if (y != 1 || (z == 0 && x == 0))
                    {
                        list.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                    }
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x1x3Centered(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int z = -1; z < 2; z++)
        {
            for (int x = -1; x < 2; x++)
            {
                list.add(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
            }
        }
        return list;
    }

        public static float getConvertedTemperature(float temp)
        {
            switch (IRConfig.Main.temperatureScale.get())
            {
                default:
                case 0:
                    return temp;
                case 1:
                    return (float) (temp * 1.8 + 32);
                case 2:
                    return (float) (temp + 273.15);
            }
        }

    public static String getTemperatureUnit()
    {
        String st;
        switch (IRConfig.Main.temperatureScale.get())
        {
            default:
            case 0:
                st = " " + I18n.format("render.industrialrenewal.c");
                break;
            case 1:
                st = " " + I18n.format("render.industrialrenewal.f");
                break;
            case 2:
                st = " " + I18n.format("render.industrialrenewal.k");
                break;
        }
        return st;
    }

    public static String formatEnergyString(int energy)
    {
        String text = energy + " FE";
        DecimalFormat form = new DecimalFormat("0.0");
        if (energy >= 1000 && energy < 1000000)
            text = form.format((float) energy / 1000) + "K FE";
        if (energy >= 1000000)
            text = form.format((float) energy / 1000000) + "M FE";

        return text;
    }

    public static String formatPreciseEnergyString(int energy)
    {
        String text = energy + " FE";
        DecimalFormat form = new DecimalFormat("0.00");
        if (energy >= 1000 && energy < 1000000)
            text = form.format((float) energy / 1000) + "K FE";
        if (energy >= 1000000)
            text = form.format((float) energy / 1000000) + "M FE";

        return text;
    }

    public static float normalize(float value, float min, float max)
    {
        return (value - min) / (max - min);
    }

    public static double getDistancePointToPoint(BlockPos pos1, BlockPos pos2)
    {
        double deltaX = pos1.getX() - pos2.getX();
        double deltaY = pos1.getY() - pos2.getY();
        double deltaZ = pos1.getZ() - pos2.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }
}
