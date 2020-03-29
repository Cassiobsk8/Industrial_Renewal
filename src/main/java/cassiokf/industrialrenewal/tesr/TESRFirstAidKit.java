package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRFirstAidKit extends TESRBase<TileEntityFirstAidKit>
{

    @Override
    public void render(TileEntityFirstAidKit te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing facing = te.getFaceDirection();

        float sidePlus = 0;
        for (int i = 0; i < 4; i++)
        {
            ItemStack stack = te.inventory.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                doTheMath(facing, x, z, 0.2, -0.2 + sidePlus);
                render3dItem(facing, te.getWorld(), xPos, y + 0.55f, zPos, stack, 0.3F, false);
            }
            sidePlus += 0.13;
        }
        sidePlus = 0;
        for (int i = 4; i < 8; i++)
        {
            ItemStack stack = te.inventory.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                doTheMath(facing, x, z, 0.2, -0.2 + sidePlus);
                render3dItem(facing, te.getWorld(), xPos, y + 0.3f, zPos, stack, 0.3F, false);
            }
            sidePlus += 0.13;
        }
    }
}
