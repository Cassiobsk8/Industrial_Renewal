package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityBulkConveyor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRBulkConveyor extends TESRBase<TileEntityBulkConveyor>
{
    @Override
    public void render(TileEntityBulkConveyor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        ItemStack stack1 = te.getStackInSlot(0);
        ItemStack stack2 = te.getStackInSlot(1);
        ItemStack stack3 = te.getStackInSlot(2);
        EnumFacing facing = te.getBlockFacing();

        if (!stack3.isEmpty())
        {
            float offset = te.getStackOffset(2, false);
            float oldOffset = te.getStackOffset(2, true);
            if (offset < 0.2f) oldOffset = 0;
            float stack3Progress = smoothAnimation(offset, oldOffset, partialTicks, false);
            //System.out.println(offset + " " + oldOffset + " " + stack3Progress);
            doTheMath(facing, x, z, 0.45 - (0.33 * stack3Progress), 0);
            render3dItem(facing, te.getWorld(), xPos, (y + te.getMinYOffset(2)) + (te.getMaxYOffset() * stack3Progress), zPos, stack3, 1, false);
        }
        if (!stack2.isEmpty())
        {
            float stack2Pos = smoothAnimation(te.getStackOffset(1, false), te.getStackOffset(1, true), partialTicks, false);
            doTheMath(facing, x, z, 0.77 - (0.33 * stack2Pos), 0);
            render3dItem(facing, te.getWorld(), xPos, (y + te.getMinYOffset(1)) + (te.getMaxYOffset() * stack2Pos), zPos, stack2, 1, false);
        }
        if (!stack1.isEmpty())
        {
            float stack1Pos = smoothAnimation(te.getStackOffset(0, false), te.getStackOffset(0, true), partialTicks, false);
            doTheMath(facing, x, z, 1.02 - (0.33 * stack1Pos), 0);
            render3dItem(facing, te.getWorld(), xPos, (y + te.getMinYOffset(0)) + (te.getMaxYOffset() * stack1Pos), zPos, stack1, 1, false);
        }
    }
}
