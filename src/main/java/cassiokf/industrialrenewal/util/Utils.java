package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils
{

    private static final Random RANDOM = new Random();

    public static void sendChatMessage(EntityPlayer player, String str)
    {
        if (player == null) Minecraft.getMinecraft().player.sendChatMessage(str);
        else player.sendMessage(new TextComponentString(str));
    }

    public static void sendConsoleMessage(String str)
    {
        System.out.println(str);
    }

    public static boolean isWood(ItemStack stack)
    {
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

    public static void dropInventoryItems(World worldIn, BlockPos pos, ItemStackHandler inventory)
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
        if (worldIn.isRemote) return;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty())
        {
            EntityItem entityitem = new EntityItem(worldIn, x + (double) f, y + (double) f1, z + (double) f2, stack.splitStack(RANDOM.nextInt(21) + 10));
            entityitem.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
            entityitem.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
            entityitem.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
            worldIn.spawnEntity(entityitem);
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

    public static IFluidHandler wrapFluidBlock(Block block, World world, BlockPos pos) {
        if (block instanceof IFluidBlock) {
            return new FluidBlockWrapper((IFluidBlock) block, world, pos);
        } else if (block instanceof BlockLiquid) {
            return new BlockLiquidWrapper((BlockLiquid) block, world, pos);
        } else {
            return new BlockWrapper(block, world, pos);
        }
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
        switch (IRConfig.MainConfig.Main.temperatureScale)
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
