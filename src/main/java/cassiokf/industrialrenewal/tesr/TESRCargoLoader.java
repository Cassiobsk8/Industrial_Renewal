package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRCargoLoader extends TESRBase<TileEntityCargoLoader>
{
    private final ItemStack arm = new ItemStack(ModItems.tambor);

    @Override
    public void render(TileEntityCargoLoader te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getBlockFacing();

            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == EnumFacing.SOUTH) armZ += 1;
            if (facing == EnumFacing.NORTH) armZ -= 1;
            if (facing == EnumFacing.EAST) armX += 1;
            if (facing == EnumFacing.WEST) armX -= 1;
            //doTheMath(facing, x, z, 0);
            render3dItem(facing, te.getWorld(), armX, y + 0.2f, armZ, arm, 2.08f, false);

            doTheMath(facing, x, z, 1.03, 0);
            renderText(facing, xPos, y + 0.93, zPos, te.getModeText(), 0.006F);

            doTheMath(facing, x, z, 1.03, 0);
            renderText(facing, xPos, y + 0.05, zPos, te.getTankText(), 0.006F);
            renderPointer(facing, xPos, y + 0.26, zPos, te.getCartFluidAngle(), pointer, 0.2F);
        }
    }
}
