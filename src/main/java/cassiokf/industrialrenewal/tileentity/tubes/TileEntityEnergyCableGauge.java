package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCableGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

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
        float totalCapacity = (float) getMaster().getMaxEnergyToTransport();
        currentAmount = Utils.normalize(currentAmount, 0, totalCapacity);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return amount * 90f;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        TileEntityEnergyCable te = null;
        if (compound.contains("masterPos") && hasWorld())
            te = (TileEntityEnergyCable) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
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
