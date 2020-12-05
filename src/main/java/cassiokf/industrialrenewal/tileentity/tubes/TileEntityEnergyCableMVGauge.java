package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableMVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableMVGauge(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
