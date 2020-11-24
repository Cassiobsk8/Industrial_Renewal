package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TEStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRStorage extends TESRBase<TEStorage> {
    @Override
    public void render(TEStorage te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.isMaster() && te.isBottom()) {
            EnumFacing facing = te.getMasterFacing();
            //Result Screen
            ItemStack stack = te.getStack();
            if (!stack.isEmpty()) {
                doTheMath(facing, x, z, 1.0, 0);
                render2dItem(facing, te.getWorld(), xPos, y + 0.04, zPos, stack, 1f, false);
            }
            doTheMath(facing, x, z, 0.98, 0);
            String formatted = TextFormatting.BLACK + (te.getCount() + " / " + te.getCapacity());
            renderText(facing, xPos, y + 0.68, zPos, formatted, 0.008F);
        }
    }
}
