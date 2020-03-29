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
            doTheMath(facing, x, z, 1.02 + te.stack3Pos, 0);
            render3dItem(facing, te.getWorld(), xPos, y + te.stack3YPos, zPos, stack3, 1, false);
        }
        if (!stack2.isEmpty())
        {
            doTheMath(facing, x, z, 1.02 + te.stack2Pos, 0);
            render3dItem(facing, te.getWorld(), xPos, y + te.stack2YPos, zPos, stack2, 1, false);
        }
        if (!stack1.isEmpty())
        {
            doTheMath(facing, x, z, 1.02 + te.stack1Pos, 0);
            render3dItem(facing, te.getWorld(), xPos, y + te.stack1YPos, zPos, stack1, 1, false);
        }
    }
}
