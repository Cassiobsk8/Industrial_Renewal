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
        int energy = getMaster().getOutPut();
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = energy / 1000 + "K FE";
        if (energy >= 1000000)
            text = energy / 1000000 + "M FE";
        return text;
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().getOutPutCount();
        float currentAmount = getMaster().getOutPut() / (outputs > 0 ? outputs : 1);
        float totalCapacity = energyContainer.getMaxOutput();
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return amount * 90f;
    }
}
