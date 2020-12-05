package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils
{

    private static final Random RANDOM = new Random();
    private static final DecimalFormat form = new DecimalFormat("0.0");
    private static final DecimalFormat preciseForm = new DecimalFormat("0.00");

    public static void sendChatMessage(PlayerEntity player, String str)
    {
        if (player == null) Minecraft.getInstance().player.sendChatMessage(str);
        else player.sendMessage(new StringTextComponent(str));
    }

    public static void sendConsoleMessage(String str)
    {
        System.out.println(str);
    }

    public static String blockPosToString(BlockPos pos)
    {
        return "X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ();
    }

    public static boolean isWood(ItemStack stack)
    {
        return true;
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
        if (worldIn.isRemote) return;
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty())
        {
            ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + (double) f, pos.getY() + (double) f1, pos.getZ() + (double) f2, stack.split(RANDOM.nextInt(21) + 10));
            entityitem.setMotion(RANDOM.nextGaussian() * 0.05000000074505806D, RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addEntity(entityitem);
        }
    }

    public static float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
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

    public static String getConvertedTemperatureString(float temp)
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
        return (int) getConvertedTemperature(temp) + st;
    }

    public static String formatEnergyString(int energy)
    {
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = form.format((float) energy / 1000) + "K FE";
        else if (energy >= 1000000 && energy < 1000000000)
            text = form.format((float) energy / 1000000) + "M FE";
        else if (energy >= 1000000000)
            text = form.format((float) energy / 1000000000) + "B FE";
        return text;
    }

    public static String formatPreciseEnergyString(int energy)
    {
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = preciseForm.format((float) energy / 1000) + "K FE";
        else if (energy >= 1000000 && energy < 1000000000)
            text = preciseForm.format((float) energy / 1000000) + "M FE";
        else if (energy >= 1000000000)
            text = preciseForm.format((float) energy / 1000000000) + "B FE";
        return text;
    }

    public static float normalize(float value, float min, float max)
    {
        return (value - min) / (max - min);
    }

    public static int getDistancePointToPoint(BlockPos pos1, BlockPos pos2)
    {
        int deltaX = pos1.getX() - pos2.getX();
        int deltaY = pos1.getY() - pos2.getY();
        int deltaZ = pos1.getZ() - pos2.getZ();
        return (int) MathHelper.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    public static boolean moveItemsBetweenInventories(IItemHandler from, IItemHandler to)
    {
        return moveItemsBetweenInventories(from, to, true, Integer.MAX_VALUE);
    }

    public static boolean moveItemsBetweenInventories(IItemHandler from, IItemHandler to, int maxStackCount)
    {
        return moveItemsBetweenInventories(from, to, true, maxStackCount);
    }

    private static boolean moveItemsBetweenInventories(IItemHandler from, IItemHandler to, boolean stackPerTick, int maxStackCount)
    {
        boolean movement = false;
        for (int i = 0; i < from.getSlots(); i++)
        {
            boolean needBreak = false;
            ItemStack stack = from.extractItem(i, maxStackCount, true);
            for (int j = 0; j < to.getSlots(); j++)
            {
                if (!stack.isEmpty() && to.isItemValid(j, stack))
                {
                    ItemStack left = to.insertItem(j, stack, false);
                    if (!ItemStack.areItemStacksEqual(stack, left))
                    {
                        int toExtract = stack.getCount() - left.getCount();
                        from.extractItem(i, toExtract, false);
                        movement = true;
                        if (stackPerTick)
                        {
                            needBreak = true;
                            break;
                        }
                    }
                }
            }
            if (stackPerTick && needBreak) break;
        }
        return movement;
    }

    public static boolean moveItemToInventory(IItemHandler from, int slot, IItemHandler to)
    {
        boolean movement = false;
        for (int j = 0; j < to.getSlots(); j++)
        {
            ItemStack stack = from.getStackInSlot(slot);
            if (!stack.isEmpty() && to.isItemValid(j, stack))
            {
                ItemStack left = to.insertItem(j, stack, false);
                if (!ItemStack.areItemStacksEqual(stack, left))
                {
                    int toExtract = stack.getCount() - left.getCount();
                    from.extractItem(slot, toExtract, false);
                    movement = true;
                }
            }
        }
        return movement;
    }
}
