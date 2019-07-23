package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipeGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityFluidPipeGauge extends TileEntityFluidPipe
{
    private float amount;

    public EnumFacing getGaugeFacing()
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockFluidPipeGauge ? state.getValue(BlockFluidPipeGauge.FACING) : EnumFacing.NORTH;
    }

    public String GetText()
    {
        return getMaster().getOutPut() + " mB/t";
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().getOutPutCount();
        float currentAmount = getMaster().getOutPut() / (outputs > 0 ? outputs : 1);
        float totalCapacity = maxOutput;
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return amount * 180f;
    }
}
