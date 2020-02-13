package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnergyCableHVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableHVGauge()
    {
        super(TileEntityRegister.ENERGY_CABLE_HV_GAUGE);
    }

    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.Main.maxHVEnergyCableTransferAmount.get();
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCableHV
                || te instanceof TileEntityEnergyCableHVGauge
                || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).getCableIn().equals(EnumCableIn.HV));
    }
}
