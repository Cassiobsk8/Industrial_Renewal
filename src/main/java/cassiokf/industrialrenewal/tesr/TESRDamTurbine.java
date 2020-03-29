package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityDamTurbine;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRDamTurbine extends TESRBase<TileEntityDamTurbine>
{
    @Override
    public void render(TileEntityDamTurbine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(facing, xPos, y + 0.36, zPos, te.getRotationText(), 0.008F);
            renderPointer(facing, xPos, y + 0.61, zPos, te.getRotationFill(), pointer, 0.3F);
        }
    }
}
