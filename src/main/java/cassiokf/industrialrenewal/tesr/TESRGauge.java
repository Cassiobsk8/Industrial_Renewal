package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityGauge;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRGauge extends TESRBase<TileEntityGauge>
{
    @Override
    public void render(TileEntityGauge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getGaugeFacing();
        doTheMath(facing, x, z, 0.6, 0);
        renderText(facing, xPos, y + 0.2, zPos, te.GetText(), 0.01F);
        renderPointer(facing, xPos, y + 0.5, zPos, te.GetTankFill(), pointer, 0.3F);
    }
}
