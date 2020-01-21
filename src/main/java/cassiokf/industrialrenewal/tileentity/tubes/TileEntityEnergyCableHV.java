package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnergyCableHV extends TileEntityEnergyCable
{
    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.MainConfig.Main.maxHVEnergyCableTransferAmount;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCableHV || te instanceof TileEntityEnergyCableHVGauge || te instanceof TileEntityCableTray;
    }
}
