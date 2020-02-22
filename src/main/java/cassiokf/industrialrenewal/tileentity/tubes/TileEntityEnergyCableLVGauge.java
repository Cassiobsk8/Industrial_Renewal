package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;

import static cassiokf.industrialrenewal.init.TileRegistration.ENERGYCABLEGAUGELV_TILE;

public class TileEntityEnergyCableLVGauge extends TileEntityEnergyCableGauge
{
    public TileEntityEnergyCableLVGauge()
    {
        super(ENERGYCABLEGAUGELV_TILE.get());
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
