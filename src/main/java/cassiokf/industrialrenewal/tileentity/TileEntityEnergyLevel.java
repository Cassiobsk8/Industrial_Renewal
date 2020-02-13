package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockEnergyLevel;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyLevel extends TileEntity
{
    private Direction baseFacing = Direction.DOWN;
    private Direction indicatorHorizontalFacing;
    private IEnergyStorage energyStorage;

    public TileEntityEnergyLevel()
    {
        super(TileEntityRegister.ENERGY_LEVEL);
    }

    public String GetText()
    {
        if (getEnergyStorage() != null)
        {
            int energy = energyStorage.getEnergyStored();
            int energy2 = energyStorage.getMaxEnergyStored();
            return Utils.formatEnergyString(energy) + " / " + Utils.formatEnergyString(energy2);
        } else return "No Battery";
    }

    public Direction getBaseFacing()
    {
        return baseFacing;
    }

    public void setBaseFacing(Direction facing)
    {
        baseFacing = facing;
        markDirty();
    }

    public Direction getGaugeFacing()
    {
        if (indicatorHorizontalFacing != null) return indicatorHorizontalFacing;
        return forceIndicatorCheck();
    }

    public Direction forceIndicatorCheck()
    {
        indicatorHorizontalFacing = getBlockState().get(BlockEnergyLevel.FACING);
        return indicatorHorizontalFacing;
    }

    public float GetTankFill() //0 ~ 180
    {
        if (getEnergyStorage() != null)
        {
            float currentAmount = energyStorage.getEnergyStored();
            float totalCapacity = energyStorage.getMaxEnergyStored();
            currentAmount = currentAmount / totalCapacity;
            return currentAmount;
        }
        return 0f;
    }

    private IEnergyStorage getEnergyStorage()
    {
        if (energyStorage != null) return energyStorage;
        return forceCheck();
    }

    public IEnergyStorage forceCheck()
    {
        TileEntity te = world.getTileEntity(pos.offset(baseFacing));
        if (te != null)
        {
            IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, baseFacing.getOpposite()).orElse(null);
            if (handler != null)
            {
                energyStorage = handler;
                return energyStorage;
            }
        }
        energyStorage = null;
        return null;
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        tag.putInt("baseFacing", baseFacing.getIndex());
        return super.write(tag);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        baseFacing = Direction.byIndex(tag.getInt("baseFacing"));
        super.read(tag);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(new CompoundNBT());
    }
}
