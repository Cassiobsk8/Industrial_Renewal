package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.enums.EnumCableIn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableLV extends TileEntityEnergyCable
{
    public TileEntityEnergyCableLV(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
