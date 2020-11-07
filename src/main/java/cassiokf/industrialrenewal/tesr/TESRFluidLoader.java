package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRFluidLoader extends TESRBase<TileEntityFluidLoader>
{
    private static final ItemStack arm = new ItemStack(ModItems.fluidLoaderArm);

    @Override
    public void render(TileEntityFluidLoader te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getBlockFacing();
            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == EnumFacing.SOUTH) armZ += te.getSlide();
            if (facing == EnumFacing.NORTH) armZ -= te.getSlide();
            if (facing == EnumFacing.EAST) armX += te.getSlide();
            if (facing == EnumFacing.WEST) armX -= te.getSlide();
            render3dItem(facing, te.getWorld(), armX, y - 0.5f, armZ, arm, 4.5f, false);

            doTheMath(facing, x, z, 1.01, 0);
            renderText(facing, xPos, y + 1.425, zPos, te.getCartName(), 0.004F);
            renderPointer(facing, xPos, y + 1.57, zPos, te.getCartFluidAngle(), pointer, 0.14F);

            doTheMath(facing, x, z, 1.01, 0);
            renderText(facing, xPos, y + 1.05, zPos, te.getTankText(), 0.004F);
            renderPointer(facing, xPos, y + 1.2, zPos, te.getTankFluidAngle(), pointer, 0.14F);
        }
    }
}
