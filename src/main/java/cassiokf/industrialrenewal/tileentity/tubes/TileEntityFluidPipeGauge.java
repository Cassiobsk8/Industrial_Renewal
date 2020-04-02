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
        return getMaster().averageFluid + " mB/t";
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().outPutCount;
        float currentAmount = Utils.normalize((float) getMaster().averageFluid / (outputs > 0 ? (float) outputs : 1f), 0, (float) maxOutput);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1f) * 180f;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        if (hasWorld() && world.isRemote)
        {
            TileEntityFluidPipe te = null;
            if (compound.hasKey("masterPos") && hasWorld())
                te = (TileEntityFluidPipe) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
            if (te != null) this.setMaster(te);
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (getMaster() != null) compound.setLong("masterPos", getMaster().getPos().toLong());
        return super.writeToNBT(compound);
    }
}
