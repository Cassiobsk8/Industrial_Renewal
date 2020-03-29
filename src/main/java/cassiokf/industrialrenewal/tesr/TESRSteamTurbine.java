package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntitySteamTurbine;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRSteamTurbine extends TESRBase<TileEntitySteamTurbine>
{
    @Override
    public void render(TileEntitySteamTurbine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            //STEAM
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(facing, xPos, y + 1.25, zPos, te.getSteamText(), 0.01F);
            renderPointer(facing, xPos, y + 1.5, zPos, te.getSteamFill(), pointer, 0.3F);
            //GENERATION
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(facing, xPos, y + 0.5, zPos, te.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.95, -0.96);
            renderPointer(facing, xPos, y + 0.67, zPos, te.getGenerationFill(), pointerLong, 0.5F);
            //WATER
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(facing, xPos, y - 0.25, zPos, te.getWaterText(), 0.01F);
            renderPointer(facing, xPos, y + 0.01, zPos, te.getWaterFill(), pointer, 0.3F);
            //ROTATION
            doTheMath(facing, x, z, 1.95, 0);
            renderText(facing, xPos, y + 1.25, zPos, te.getRotationText(), 0.01F);
            renderPointer(facing, xPos, y + 1.5, zPos, te.getRotationFill(), pointer, 0.3F);
            //ENERGY
            doTheMath(facing, x, z, 1.95, +1.165);
            renderText(facing, xPos, y + 0.1, zPos, te.getEnergyText(), 0.01F);
            renderBarLevel(facing, xPos, y + 0.184, zPos, te.getEnergyFill(), 1.2F);
        }
    }
}
