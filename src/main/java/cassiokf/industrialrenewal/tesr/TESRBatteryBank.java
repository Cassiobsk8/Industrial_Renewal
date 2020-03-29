package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRBatteryBank extends TESRBase<TileEntityBatteryBank>
{
    @Override
    public void render(TileEntityBatteryBank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getBlockFacing();
        doTheMath(facing, x, z, 1.023, 0);
        renderText(facing, xPos, y + 0.43, zPos, te.GetText(), 0.005F);
        renderBarLevel(facing, xPos, y + 0.49, zPos, te.getBatteryFill(), 0.7F);
    }
}
