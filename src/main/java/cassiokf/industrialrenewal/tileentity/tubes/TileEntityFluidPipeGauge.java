package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipeGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class TileEntityFluidPipeGauge extends TileEntityFluidPipe
{
    private float amount;
    private Direction facing;

    public TileEntityFluidPipeGauge(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public Direction getGaugeFacing()
    {
        if (facing != null) return facing;
        BlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockFluidPipeGauge
                ? state.get(BlockFluidPipeGauge.FACING) : Direction.NORTH;
        return facing;
    }

    public String GetText()
    {
        return getMaster().averageFluid + " mB/t";
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().outPutCount;
        float currentAmount = Utils.normalizeClamped((float) getMaster().averageFluid / (outputs > 0 ? (float) outputs : 1f), 0, (float) maxOutput);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1f) * 180f;
    }
}
