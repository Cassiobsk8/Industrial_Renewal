package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;

import static cassiokf.industrialrenewal.init.TileRegistration.ENERGYCABLEGAUGEHV_TILE;

public class TileEntityEnergyCableHVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableHVGauge()
    {
        super(ENERGYCABLEGAUGEHV_TILE.get());
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
