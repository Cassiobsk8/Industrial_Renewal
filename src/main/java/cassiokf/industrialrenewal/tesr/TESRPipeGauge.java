package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeGauge;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRPipeGauge extends TESRBase<TileEntityFluidPipeGauge>
{
    @Override
    public void render(TileEntityFluidPipeGauge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getGaugeFacing();
        doTheMath(facing, x, z, 0.72, 0);
        renderText(facing, xPos, y + 0.56, zPos, te.GetText(), 0.009F);
        renderPointer(facing, xPos, y + 0.73, zPos, te.getOutPutAngle(), pointer, 0.16F);
    }
}
