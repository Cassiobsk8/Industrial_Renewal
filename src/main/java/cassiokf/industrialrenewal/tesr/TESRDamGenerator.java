package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityDamGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRDamGenerator extends TESRBase<TileEntityDamGenerator>
{
    @Override
    public void render(TileEntityDamGenerator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            //GENERATION
            doTheMath(facing, x, z, 1.98, 0);
            renderText(facing, xPos, y + 0.43, zPos, te.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.98, 0.115);
            renderPointer(facing, xPos, y + 0.58, zPos, te.getGenerationFill(), pointerLong, 0.5F);
        }
    }
}
