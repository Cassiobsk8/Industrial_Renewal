package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityWindTurbinePillar;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRWindTurbinePillar extends TESRBase<TileEntityWindTurbinePillar>
{
    @Override
    public void render(TileEntityWindTurbinePillar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isBase())
        {
            EnumFacing facing = te.getBlockFacing();
            doTheMath(facing, x, z, 0.78, 0);
            renderText(facing, xPos, y + 0.72, zPos, te.getText(), 0.006F);
            doTheMath(facing, x, z, 0.78, 0.1f);
            renderPointer(facing, xPos, y + 0.845, zPos, te.getGenerationForGauge(), pointerLong, 0.38F);
            renderPointer(facing, xPos, y + 0.845, zPos, te.getPotentialValue(), limiter, 0.57F);
        }
    }
}
