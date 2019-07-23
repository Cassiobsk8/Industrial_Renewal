package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.BlockFluidPipeGauge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityFluidPipeGauge extends TileEntityFluidPipe
{

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
        float currentAmount = getMaster().getOutPut();
        float totalCapacity = maxOutput;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }
}
