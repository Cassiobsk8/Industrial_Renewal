package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnergyCableMVGauge extends TileEntityEnergyCableGauge
{
    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.MainConfig.Main.maxMVEnergyCableTransferAmount;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCableMV;
    }
}
