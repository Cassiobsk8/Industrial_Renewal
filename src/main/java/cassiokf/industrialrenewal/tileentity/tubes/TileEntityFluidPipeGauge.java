package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipeGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityFluidPipeGauge extends TileEntityFluidPipe
{
    private float amount;
    private EnumFacing facing;

    public EnumFacing getGaugeFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockFluidPipeGauge
                ? state.getValue(BlockFluidPipeGauge.FACING) : EnumFacing.NORTH;
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
    public void readFromNBT(NBTTagCompound compound)
    {
        TileEntityFluidPipe te = null;
        if (compound.hasKey("masterPos") && hasWorld())
            te = (TileEntityFluidPipe) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
        if (te != null) this.setMaster(te);
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (getMaster() != null) compound.setLong("masterPos", getMaster().getPos().toLong());
        return super.writeToNBT(compound);
    }
}
