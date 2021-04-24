package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCableGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileEntityEnergyCableGauge extends TileEntityEnergyCable
{
    private float amount;
    private Direction facing;

    public TileEntityEnergyCableGauge(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public Direction getGaugeFacing()
    {
        if (facing != null) return facing;
        BlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockEnergyCableGauge
                ? state.get(BlockEnergyCableGauge.FACING) : Direction.NORTH;
        return facing;
    }

    public String GetText()
    {
        return Utils.formatPreciseEnergyString(getMaster().averageEnergy) + "/t";
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().outPutCount;
        float currentAmount = (float) getMaster().averageEnergy / (outputs > 0 ? (float) outputs : 1f);
        float totalCapacity = (float) energyContainer.getMaxOutput();
        currentAmount = Utils.normalizeClamped(currentAmount, 0, totalCapacity);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1f) * 90f;
    }
}
