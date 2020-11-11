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

            doTheMath(facing, x, z, 1.99, -1.29);
            renderScreenTexts(facing, xPos, y + 1.4, zPos, te.getScreenTexts(), 0.1f, 0.004F);

            doTheMath(facing, x, z, 1.96, -1.15);
            renderText(facing, xPos, y + 0.165, zPos, te.getEnergyText(2), 0.007F);
            renderBarLevel(facing, xPos, y + 0.22, zPos, te.getEnergyFill(), 0.7f);

            doTheMath(facing, x, z, 1.95, +1.16);
            renderText(facing, xPos, y + 0.18, zPos, te.getWaterText(1), 0.008F);
            renderText(facing, xPos, y + 0.07, zPos, te.getWaterText(2), 0.008F);
            renderPointer(facing, xPos, y + 0.43, zPos, te.getWaterFill(), pointer, 0.3F);

            //indicator
            doTheMath(facing, x, z, 1.96, -0.8);
            render3dItem(facing, te.getWorld(), xPos, y + 0.5, zPos, getIndicator(te.isRunning()), 1f, true);
            //switch
            doTheMath(facing, x, z, 1.96, -0.8);
            render3dItem(facing, te.getWorld(), xPos, y + 0.32, zPos, getSwitch(te.isRunning()), 1f, true);
        }
    }
}
