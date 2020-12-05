package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockEnergyLevel;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyLevel extends TEBase
{
    private Direction baseFacing = Direction.DOWN;
    private Direction indicatorHorizontalFacing;
    private IEnergyStorage energyStorage;

    public TileEntityEnergyLevel(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public String GetText()
    {
        if (getEnergyStorage() != null)
        {
            int energy = energyStorage.getEnergyStored();
            String text = energy + "";
            if (energy >= 1000 && energy < 1000000)
                text = energy / 1000 + "K";
            if (energy >= 1000000)
                text = energy / 1000000 + "M";

            int energy2 = energyStorage.getMaxEnergyStored();
            String textM = energy2 + " FE";
            if (energy >= 1000 && energy2 < 1000000)
                textM = energy2 / 1000 + "K FE";
            if (energy2 >= 1000000)
                textM = energy2 / 1000000 + "M FE";

            return text + " / " + textM;
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
        indicatorHorizontalFacing = this.world.getBlockState(this.pos).get(BlockEnergyLevel.FACING);
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
    public void read(final CompoundNBT tag)
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
