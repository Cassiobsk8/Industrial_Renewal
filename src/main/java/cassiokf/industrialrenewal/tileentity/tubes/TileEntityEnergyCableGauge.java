package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCableGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityEnergyCableGauge extends TileEntityEnergyCable
{
    private float amount;
    private EnumFacing facing;

    public EnumFacing getGaugeFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockEnergyCableGauge
                ? state.getValue(BlockEnergyCableGauge.FACING) : EnumFacing.NORTH;
        Sync();
        return facing;
    }

    public String GetText()
    {
        int energy = getMaster().getOutPut();
        return Utils.formatPreciseEnergyString(energy) + "/t";
    }

    public float getOutPutAngle()
    {
        int outputs = getMaster().getOutPutCount();
        float currentAmount = (float) getMaster().getOutPut() / (outputs > 0 ? (float) outputs : 1f);
        float totalCapacity = (float) getMaster().energyContainer.getMaxOutput();
        currentAmount = Utils.normalize(currentAmount, 0, totalCapacity);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return amount * 90f;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        TileEntityEnergyCable te = null;
        if (compound.hasKey("masterPos") && hasWorld())
            te = (TileEntityEnergyCable) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
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
