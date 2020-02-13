package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipeGauge;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntityFluidPipeBaseGauge extends TileEntityFluidPipeBase
{
    private float amount;
    private Direction facing;

    public TileEntityFluidPipeBaseGauge()
    {
        super(TileEntityRegister.FLUID_PIPE_GAUGE);
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
        return getMaster().getOutPut() + " mB/t";
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().getOutPutCount();
        float currentAmount = (float) getMaster().getOutPut() / (outputs > 0 ? (float) outputs : 1f);
        float totalCapacity = (float) maxOutput;
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return amount * 180f;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        TileEntityFluidPipeBase te = null;
        if (compound.contains("masterPos") && hasWorld())
            te = (TileEntityFluidPipeBase) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
        if (te != null) this.setMaster(te);
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        if (getMaster() != null) compound.putLong("masterPos", getMaster().getPos().toLong());
        return super.write(compound);
    }
}
