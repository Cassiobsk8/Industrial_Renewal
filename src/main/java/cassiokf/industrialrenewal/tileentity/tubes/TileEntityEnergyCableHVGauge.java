package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnergyCableHVGauge extends TileEntityEnergyCableGauge
{
    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.MainConfig.Main.maxHVEnergyCableTransferAmount;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCableHV;
    }
}
