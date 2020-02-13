package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnergyCableMVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableMVGauge()
    {
        super(TileEntityRegister.ENERGY_CABLE_MV_GAUGE);
    }

    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.Main.maxMVEnergyCableTransferAmount.get();
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCableMV
                || te instanceof TileEntityEnergyCableMVGauge
                || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).getCableIn().equals(EnumCableIn.MV));
    }
}
