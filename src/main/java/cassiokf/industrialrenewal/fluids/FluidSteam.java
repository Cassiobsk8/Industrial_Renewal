package cassiokf.industrialrenewal.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class FluidSteam extends Fluid
{
    public FluidSteam(String fluidName, ResourceLocation still, ResourceLocation flowing)
    {
        super(fluidName, still, flowing, Color.WHITE);
        this.setUnlocalizedName(fluidName);
        this.density = -1000;
        this.temperature = 380;
        this.viscosity = 500;
        this.isGaseous = true;
    }


}
