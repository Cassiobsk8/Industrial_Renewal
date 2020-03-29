package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamBoiler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRSteamBoiler extends TESRBase<TileEntitySteamBoiler>
{
    private final ItemStack fire = new ItemStack(ModItems.fire);

    @Override
    public void render(TileEntitySteamBoiler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            //WATER
            doTheMath(facing, x, z, 1.9, -0.69);
            renderText(facing, xPos, y + 0.25, zPos, te.getWaterText(), 0.01F);
            renderPointer(facing, xPos, y + 0.51, zPos, te.GetWaterFill(), pointer, 0.3F);
            //STEAM
            doTheMath(facing, x, z, 1.9, 0.69);
            renderText(facing, xPos, y + 0.25, zPos, te.getSteamText(), 0.01F);
            renderPointer(facing, xPos, y + 0.51, zPos, te.GetSteamFill(), pointer, 0.3F);
            //ENERGY
            doTheMath(facing, x, z, 1.9, 0);
            renderText(facing, xPos, y + 0.18, zPos, te.getFuelText(), 0.01F);
            renderPointer(facing, xPos, y + 0.44, zPos, te.getFuelFill(), pointer, 0.3F);
            //HEAT
            doTheMath(facing, x, z, 1.9, 0);
            renderText(facing, xPos, y + 0.93, zPos, te.getHeatText(), 0.01F);
            renderPointer(facing, xPos, y + 1.19, zPos, te.getHeatFill(), pointer, 0.3F);
            //Fire
            if (te.getType() > 0 && te.getFuelTime() > 0)
            {
                doTheMath(facing, x, z, 1.9, 0);
                render3dItem(facing, te.getWorld(), xPos, y - 0.7, zPos, fire, 1, true);
            }
        }
    }
}
