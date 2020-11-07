package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRRecordPlayer extends TESRBase<TileEntityRecordPlayer>
{
    private static final ItemStack discR = new ItemStack(ModItems.discR);

    @Override
    public void render(TileEntityRecordPlayer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        float distanceOffset = 0.13f;
        double startY = y - 0.25f;
        if (te.hasDiskInSlot(0))
        {
            render3dItem(EnumFacing.NORTH, te.getWorld(), x + 0.5f, startY, z + 0.5f, discR, 4f, false);
        }
        startY += distanceOffset;
        if (te.hasDiskInSlot(1))
        {
            render3dItem(EnumFacing.NORTH, te.getWorld(), x + 0.5f, startY, z + 0.5f, discR, 4f, false);
        }
        startY += distanceOffset;
        if (te.hasDiskInSlot(2))
        {
            render3dItem(EnumFacing.NORTH, te.getWorld(), x + 0.5f, startY, z + 0.5f, discR, 4f, false);
        }
        startY += distanceOffset;
        if (te.hasDiskInSlot(3))
        {
            render3dItem(EnumFacing.NORTH, te.getWorld(), x + 0.5f, startY, z + 0.5f, discR, 4f, false);
        }
    }
}
