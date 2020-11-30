package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.item.ItemCoilHV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GUIWire extends Gui
{
    public GUIWire(Minecraft mc)
    {
        EntityPlayer player = mc.player;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof ItemCoilHV)
        {
            String text = ((ItemCoilHV) stack.getItem()).getDistanceText(player);
            if (!text.equals(""))
            {
                ScaledResolution scaled = new ScaledResolution(mc);
                int width = scaled.getScaledWidth();
                int height = scaled.getScaledHeight();

                drawCenteredString(mc.fontRenderer, text, width / 2, (height) - 50, Integer.parseInt("FFAA00", 16));
            }
        }
    }
}
