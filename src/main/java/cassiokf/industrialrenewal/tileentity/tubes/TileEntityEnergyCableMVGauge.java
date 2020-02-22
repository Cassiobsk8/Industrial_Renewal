package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;

import static cassiokf.industrialrenewal.init.TileRegistration.ENERGYCABLEGAUGEMV_TILE;

public class TileEntityEnergyCableMVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableMVGauge()
    {
        super(ENERGYCABLEGAUGEMV_TILE.get());
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
