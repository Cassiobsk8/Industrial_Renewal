package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRTransformerHV extends TESRBase<TileEntityTransformerHV>
{
    @Override
    public void render(TileEntityTransformerHV te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            //CABLE RENDER
            if (te.isConnected())
            {
                TESRWire.renderWire(te.getConnectorPos(), te.getConnectionPos(), x, y + 1D, z);
            }

            EnumFacing facing = te.getMasterFacing();
            //GENERATION
            doTheMath(facing, x, z, 1.86, 0);
            renderText(facing, xPos, y + 0.16, zPos, te.getGenerationText(), 0.008F);
            doTheMath(facing, x, z, 1.84, 0.13);
            renderPointer(facing, xPos, y + 0.36, zPos, te.getGenerationFill(), pointerLong, 0.5F);
        }
    }
}
