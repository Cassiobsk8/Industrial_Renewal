package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityMining;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRMining extends TESRBase<TileEntityMining>
{
    @Override
    public void render(TileEntityMining te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            if (te.hasDrill())
            {
                render3dItem(facing, te.getWorld(), x + 0.5, y - 0.9f - te.getSlide(), z + 0.5, te.getDrill(), 4.5f, false, te.getRotation(), 0, 1, 0);
            }

            doTheMath(facing, x, z, 1.95, -1.14);
            renderText(facing, xPos, y + 0.18, zPos, te.getEnergyText(1), 0.008F);
            renderText(facing, xPos, y + 0.07, zPos, te.getEnergyText(2), 0.008F);
            renderPointer(facing, xPos, y + 0.43, zPos, te.getEnergyFill(), pointer, 0.3F);

            doTheMath(facing, x, z, 1.95, +1.16);
            renderText(facing, xPos, y + 0.18, zPos, te.getWaterText(1), 0.008F);
            renderText(facing, xPos, y + 0.07, zPos, te.getWaterText(2), 0.008F);
            renderPointer(facing, xPos, y + 0.43, zPos, te.getWaterFill(), pointer, 0.3F);

            doTheMath(facing, x, z, 1.95, 0);
            renderText(facing, xPos, y + 1.3, zPos, te.getHeatText(), 0.008F);
            renderPointer(facing, xPos, y + 1.57, zPos, te.getHeatFill(), pointer, 0.3F);
        }
    }
}
