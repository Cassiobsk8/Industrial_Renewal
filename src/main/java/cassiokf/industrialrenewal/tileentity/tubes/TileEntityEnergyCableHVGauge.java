package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableHVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableHVGauge(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
