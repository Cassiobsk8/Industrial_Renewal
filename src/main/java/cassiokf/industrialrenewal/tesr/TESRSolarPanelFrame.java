package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelFrame;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRSolarPanelFrame extends TESRBase<TileEntitySolarPanelFrame>
{
    @Override
    public void render(TileEntitySolarPanelFrame te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.hasPanel())
        {
            EnumFacing facing = te.getBlockFacing();
            doTheMath(facing, x, z, 0.4, 0);
            render3dItem(facing, te.getWorld(), xPos, y + 0.45f, zPos, te.getPanel(), 4, false, 22.5f, 1, 0, 0);
        }
    }
}
