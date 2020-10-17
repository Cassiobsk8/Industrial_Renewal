package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCableGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityEnergyCableGauge extends TileEntityEnergyCable
{
    private float amount;
    private EnumFacing facing;
    private TileEntityEnergyCable cMaster;

    public EnumFacing getGaugeFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockEnergyCableGauge
                ? state.getValue(BlockEnergyCableGauge.FACING) : EnumFacing.NORTH;
        return facing;
    }

    public String GetText()
    {
        return Utils.formatPreciseEnergyString(getMaster().averageEnergy) + "/t";
    }

    public float getOutPutAngle()
    {
        if (cMaster == null) return 0f;
        int outputs = cMaster.outPutCount;
        float currentAmount = (float) cMaster.averageEnergy / (outputs > 0 ? (float) outputs : 1f);
        float totalCapacity = (float) energyContainer.getMaxOutput();
        currentAmount = Utils.normalize(currentAmount, 0, totalCapacity);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1f) * 90f;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        if (hasWorld() && world.isRemote)
        {
            TileEntity te = null;
            if (compound.hasKey("masterPos"))
                te =  world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
            if (te instanceof TileEntityEnergyCable) cMaster = (TileEntityEnergyCable) te;
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (hasWorld() && !world.isRemote) compound.setLong("masterPos", getMaster().getPos().toLong());
        return super.writeToNBT(compound);
    }
}
