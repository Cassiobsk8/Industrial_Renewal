package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityPortableGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRPortableGenerator extends TESRBase<TileEntityPortableGenerator>
{
    @Override
    public void render(TileEntityPortableGenerator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getBlockFacing();
        doTheMath(facing, x, z, 1.02, -0.26);
        renderText(facing, xPos, y + 0.514, zPos, te.getTankText(), 0.005F);
        renderPointer(facing, xPos, y + 0.68, zPos, te.getTankFill(), pointer, 0.2F);

        doTheMath(facing, x, z, 1.02, 0.27);
        renderText(facing, xPos, y + 0.514, zPos, te.getEnergyText(), 0.005F);
        doTheMath(facing, x, z, 1.02, 0.332);
        renderPointer(facing, xPos, y + 0.627, zPos, te.getEnergyFill(), pointerLong, 0.35F);
        //indicator
        doTheMath(facing, x, z, 1.02, 0);
        render3dItem(facing, te.getWorld(), xPos, y + 0.77, zPos, getIndicator(te.isWorking()), 1f, true);
        //switch
        doTheMath(facing, x, z, 1.02, 0);
        render3dItem(facing, te.getWorld(), xPos, y + 0.62, zPos, getSwitch(te.isWorking()), 1f, true);
    }
}
