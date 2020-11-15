package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TEFluidTank;
import net.minecraft.util.EnumFacing;

public class TESRFluidTank extends TESRBase<TEFluidTank>
{

    @Override
    public void render(TEFluidTank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster() && te.isBottom())
        {
            EnumFacing facing = te.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(facing, xPos, y + 0.36, zPos, te.getFluidName(), 0.008F);
            renderPointer(facing, xPos, y + 0.63, zPos, te.getFluidAngle(), pointer, 0.3F);
        }
    }
}
