package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGauge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyLevel extends TileEntity
{
    private EnumFacing baseFacing = EnumFacing.DOWN;
    private EnumFacing indicatorHorizontalFacing;
    private IEnergyStorage energyStorage;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
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

    public EnumFacing getBaseFacing()
    {
        return baseFacing;
    }

    public void setBaseFacing(EnumFacing facing)
    {
        baseFacing = facing;
        markDirty();
    }

    public EnumFacing getGaugeFacing()
    {
        if (indicatorHorizontalFacing != null) return indicatorHorizontalFacing;
        return forceIndicatorCheck();
    }

    public EnumFacing forceIndicatorCheck()
    {
        indicatorHorizontalFacing = this.world.getBlockState(this.pos).getValue(BlockGauge.FACING);
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
        TileEntity te = this.world.getTileEntity(pos.offset(baseFacing));
        if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, baseFacing.getOpposite()))
        {
            IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, baseFacing.getOpposite());
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
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setInteger("baseFacing", baseFacing.getIndex());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        baseFacing = EnumFacing.byIndex(tag.getInteger("baseFacing"));
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }
}
