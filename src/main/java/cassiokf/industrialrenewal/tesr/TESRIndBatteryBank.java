package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.machines.TEIndustrialBatteryBank;
import net.minecraft.util.EnumFacing;

public class TESRIndBatteryBank extends TESRBase<TEIndustrialBatteryBank>
{
    @Override
    public void render(TEIndustrialBatteryBank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster() && te.isBottom())
        {
            EnumFacing facing = te.getMasterFacing();
            doTheMath(facing, x, z, 1.97, -0.72);
            renderText(facing, xPos, y + 0.2, zPos, te.getEnergyText(), 0.006F);
            renderBarLevel(facing, xPos, y + 0.34, zPos, te.getBatteryFill(), 1.2F);

            doTheMath(facing, x, z, 1.97, 0.85f);
            renderPointer(facing, xPos, y + 0.486, zPos, te.getOutPutAngle(), pointerLong, 0.6F);
            doTheMath(facing, x, z, 1.97, 0.72f);
            renderText(facing, xPos, y + 0.21, zPos, te.getOutPutText(), 0.008F);
        }
    }
}
