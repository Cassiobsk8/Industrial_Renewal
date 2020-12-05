package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableMV extends TileEntityEnergyCable
{
    public TileEntityEnergyCableMV(TileEntityType<?> tileEntityTypeIn)
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
