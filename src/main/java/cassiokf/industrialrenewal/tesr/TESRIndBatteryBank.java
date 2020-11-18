package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.machines.TEIndustrialBatteryBank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TESRIndBatteryBank extends TESRBase<TEIndustrialBatteryBank>
{
    private static final ItemStack lBattery = new ItemStack(ModItems.battery_lithium);
    @Override
    public void render(TEIndustrialBatteryBank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            if (te.isBottom())
            {
                doTheMath(facing, x, z, 1.97, -0.586);
                renderPointer(facing, xPos, y + 0.486, zPos, te.getInPutAngle(), pointerLong, 0.6F);
                doTheMath(facing, x, z, 1.97, -0.72);
                renderText(facing, xPos, y + 0.21, zPos, te.getInPutText(), 0.008F);
                render3dItem(facing, te.getWorld(), xPos, y + 0.96f, zPos, label_5, 1.6f, false);
                renderText(facing, xPos, y + 0.984f, zPos, te.getInPutIndicatorText(), 0.008F);

                doTheMath(facing, x, z, 1.97, 0);
                renderText(facing, xPos, y + 1.0, zPos, te.getEnergyText(), 0.006F);
                renderBarLevel(facing, xPos, y + 1.14, zPos, te.getBatteryFill(), 1.2F);

                doTheMath(facing, x, z, 1.97, 0.846f);
                renderPointer(facing, xPos, y + 0.486, zPos, te.getOutPutAngle(), pointerLong, 0.6F);
                doTheMath(facing, x, z, 1.97, 0.72f);
                renderText(facing, xPos, y + 0.21, zPos, te.getOutPutText(), 0.008F);
                render3dItem(facing, te.getWorld(), xPos, y + 0.96f, zPos, label_5, 1.6f, false);
                renderText(facing, xPos, y + 0.984f, zPos, te.getOutPutIndicatorText(), 0.008F);
            }
            int quantity = te.getBatteries();
            renderBatteries(te.getWorld(), quantity, facing, x, y, z);
        }
    }

    private void renderBatteries(World world, int quantity, EnumFacing facing, Double x, Double y, Double z)
    {
        if (quantity > 0)
        {
            float offset = 1.3f;
            float spacing = 0.83f;
            float yOffset = 0.46f;
            float ySpacing = 0.67f;

            //Left Side
            float yOff = yOffset;
            for (int r = 0; r < 4; r++)
            {
                float off = offset;
                for (int zb = 0; zb < 3; zb++)
                {
                    doTheMath(facing, x, z, off, -0.29f);
                    render3dItem(facing, world, xPos, y - yOff, zPos, lBattery, 1.7f, false, true, -90, 1, 0, 0, false, true);
                    off -= spacing;
                    quantity--;
                    if (quantity == 0) return;
                }
                yOff -= ySpacing;
            }
            //Right Side
            yOff = yOffset;
            for (int r = 0; r < 4; r++)
            {
                float off = offset;
                for (int zb = 0; zb < 3; zb++)
                {
                    doTheMath(facing, x, z, off, 0.29f);
                    render3dItem(facing, world, xPos, y - yOff, zPos, lBattery, 1.7f, false, true, 90, 1, 0, 0, false, true);
                    off -= spacing;
                    quantity--;
                    if (quantity == 0) return;
                }
                yOff -= ySpacing;
            }
        }
    }
}
