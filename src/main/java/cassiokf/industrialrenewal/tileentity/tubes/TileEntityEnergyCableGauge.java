package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCableGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityEnergyCableGauge extends TileEntityEnergyCable
{
    private float amount;

    public EnumFacing getGaugeFacing()
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockEnergyCableGauge ? state.getValue(BlockEnergyCableGauge.FACING) : EnumFacing.NORTH;
    }

    public String GetText()
    {
        return getMaster().getOutPut() + " FE/t";
    }

    public float getOutPutAngle()
    {
        float currentAmount = getMaster().getOutPut();
        float totalCapacity = energyContainer.getMaxOutput();
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return amount * 90f;
    }
}
