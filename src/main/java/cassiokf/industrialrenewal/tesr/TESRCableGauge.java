package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableGauge;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRCableGauge extends TESRBase<TileEntityEnergyCableGauge>
{

    @Override
    public void render(TileEntityEnergyCableGauge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getGaugeFacing();
        doTheMath(facing, x, z, 0.69, 0.12f);
        renderPointer(facing, xPos, y + 0.645, zPos, te.getOutPutAngle(), pointerLong, 0.4F);
        doTheMath(facing, x, z, 0.69, 0);
        renderText(facing, xPos, y + 0.48, zPos, te.GetText(), 0.008F);
    }
}
