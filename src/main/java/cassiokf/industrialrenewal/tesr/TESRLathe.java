package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.machines.TELathe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRLathe extends TESRBase<TELathe>
{
    @Override
    public void render(TELathe te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            //Result Screen
            ItemStack result = te.getResultItem();
            if (result != null && !result.isEmpty())
            {
                doTheMath(facing, x, z, 0.97, 1.1);
                String formated = TextFormatting.GREEN + te.getResultItem().getDisplayName();
                renderText(facing, xPos, y + 1.1, zPos, formated, 0.005F);
                doTheMath(facing, x, z, 0.97, 1.1);
                render3dItem(facing, te.getWorld(), xPos, y + 1.2, zPos, result, 0.5f, true);
            }
            //Processing Item
            ItemStack processing = te.getProcessingItem();
            if (processing != null && !processing.isEmpty())
            {
                doTheMath(facing, x, z, 0.13, 0);
                render3dItem(facing, te.getWorld(), xPos, y + 1.05, zPos, te.getProcessingItem(), 1, true);
            }
            //Cutter
            float progress = smoothAnimation(te.getNormalizedProcess(), te.getOldProcess(), partialTicks, false);
            doTheMath(facing, x, z, 0.5, 0.05 + progress);
            render3dItem(facing, te.getWorld(), xPos, y - 0.25, zPos, cutter, 4, true);
        }
    }
}
