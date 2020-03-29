package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityEnergyLevel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESREnergyLevelGauge extends TESRBase<TileEntityEnergyLevel>
{
    @Override
    public void render(TileEntityEnergyLevel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getGaugeFacing();
        doTheMath(facing, x, z, 0.6, 0);
        renderText(facing, xPos, y + 0.18, zPos, te.GetText(), 0.008F);
        renderBarLevel(facing, xPos, y + 0.27, zPos, te.GetTankFill(), 1.2F);
    }
}
