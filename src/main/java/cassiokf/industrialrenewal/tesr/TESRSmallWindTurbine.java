package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntitySmallWindTurbine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRSmallWindTurbine extends TESRBase<TileEntitySmallWindTurbine>
{
    private static ItemStack blade = new ItemStack(ModItems.windBlade);

    @Override
    public void render(TileEntitySmallWindTurbine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (te.hasBlade())
        {
            //shift = shiftOld + (shift - shiftOld) * partialTick
            EnumFacing facing = te.getBlockFacing();
            doTheMath(facing, x, z, 0, 0);
            float rotation = smoothAnimation(te.getRotation(), te.getOldRotation(), partialTicks, true);
            render3dItemRotatable(facing, te.getWorld(), xPos, y + 0.5f, zPos, blade, 12, false, true, rotation, 0, 1, 0, true);
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntitySmallWindTurbine te)
    {
        return true;
    }
}
