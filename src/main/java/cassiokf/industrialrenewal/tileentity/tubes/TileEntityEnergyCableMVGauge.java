package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
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
        return te instanceof TileEntityEnergyCableMV
                || te instanceof TileEntityEnergyCableMVGauge
                || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).getCableIn().equals(EnumCableIn.MV));
    }
}
