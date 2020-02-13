package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnergyCableLV extends TileEntityEnergyCable
{
    public TileEntityEnergyCableLV()
    {
        super(TileEntityRegister.ENERGY_CABLE_LV);
    }

    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.Main.maxLVEnergyCableTransferAmount.get();
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCableLV
                || te instanceof TileEntityEnergyCableLVGauge
                || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).getCableIn().equals(EnumCableIn.LV));
    }
}
