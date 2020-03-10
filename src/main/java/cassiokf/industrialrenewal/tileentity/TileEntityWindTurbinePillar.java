package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityWindTurbinePillar extends TileEntityMultiBlocksTube<TileEntityWindTurbinePillar>
{
    private final VoltsEnergyContainer energyContainer;
    private final VoltsEnergyContainer dummyEnergyContainer;

    private float amount;//For Lerp
    private int oldOutPut = -1;
    private int potentialEnergy;
    private int oldPotential = -1;

    private EnumFacing facing;

    private EnumFacing[] faces = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
    private boolean isBase;

    public TileEntityWindTurbinePillar()
    {
        this.energyContainer = new VoltsEnergyContainer(1024, 128, 128)
        {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate)
            {
                return TileEntityWindTurbinePillar.this.passEnergy(maxReceive, simulate);
            }
        };
        this.dummyEnergyContainer = new VoltsEnergyContainer(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }

            @Override
            public boolean canExtract()
            {
                return false;
            }
        };
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        isBase = getIsBase();
    }

    public int passEnergy(int amount, boolean simulate)
    {
        if (!isMaster() && !isMasterInvalid()) return getMaster().passEnergy(amount, simulate);

        int out = 0;
        int validOutputs = getOutputCount();
        potentialEnergy = Math.min(amount, this.energyContainer.getMaxOutput());
        if (validOutputs == 0)
        {
            outPut = 0;
            if (outPut != oldOutPut || potentialEnergy != oldPotential)
            {
                oldPotential = potentialEnergy;
                oldOutPut = outPut;
                this.Sync();
            }
            return 0;
        }

        int realMaxOutput = Math.min(amount / validOutputs, this.energyContainer.getMaxOutput());
        for (BlockPos posM : getPosSet().keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = getPosSet().get(posM).getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    int energy = energyStorage.receiveEnergy(realMaxOutput, simulate);

                    out += energy;
                }
            }
        }
        if (!simulate)
        {
            outPut = out;
        } else if (out == 0)
        {
            outPut = out;
        }

        if (outPut != oldOutPut || potentialEnergy != oldPotential)
        {
            oldPotential = potentialEnergy;
            oldOutPut = outPut;
            this.Sync();
        }
        return out;
    }

    public int getOutputCount()
    {
        int canAccept = 0;
        int realMaxOutput = this.energyContainer.getMaxOutput();
        for (BlockPos posM : getPosSet().keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = getPosSet().get(posM).getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    int energy = energyStorage.receiveEnergy(realMaxOutput, true);
                    if (energy > 0) canAccept++;
                }
            }
        }
        return canAccept;
    }

    @Override
    public EnumFacing[] getFacesToCheck()
    {
        return faces;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityWindTurbinePillar;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        isBase = getIsBase();
        outPut = 0;
        potentialEnergy = 0;
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.HORIZONTALS)
        {
            BlockPos currentPos = pos.offset(face);
            if (isBase)
            {
                IBlockState state = world.getBlockState(currentPos);
                TileEntity te = world.getTileEntity(currentPos);
                boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar)
                        && te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite());

                if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).canReceive())
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                    else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
            } else
            {
                if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
            }
        }
        this.Sync();
    }

    public EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockWindTurbinePillar
                ? state.getValue(BlockWindTurbinePillar.FACING) : EnumFacing.NORTH;
        return facing;
    }

    public float getGenerationforGauge()
    {
        float currentAmount = getEnergyGenerated();
        float totalCapacity = this.energyContainer.getMaxOutput();
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public float getPotentialValue()
    {
        float currentAmount = potentialEnergy;
        float totalCapacity = this.energyContainer.getMaxOutput();
        currentAmount = currentAmount / totalCapacity;
        return Math.min(currentAmount, 1) * 90f;
    }

    public int getEnergyGenerated()
    {
        return getMaster().outPut;
    }

    public String getText()
    {
        return getEnergyGenerated() + " FE/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        IBlockState state = world.getBlockState(pos.down());
        return !(state.getBlock() instanceof BlockWindTurbinePillar);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.UP || isBase())) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.UP))
            return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
        if (capability == CapabilityEnergy.ENERGY && (isBase()))
            return CapabilityEnergy.ENERGY.cast(dummyEnergyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.isBase = compound.getBoolean("base");
        if (hasWorld() && world.isRemote) this.potentialEnergy = compound.getInteger("potential");
        if (hasWorld() && world.isRemote) this.outPut = compound.getInteger("outPut");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("base", this.isBase);
        compound.setInteger("potential", potentialEnergy);
        compound.setInteger("outPut", outPut);
        return super.writeToNBT(compound);
    }
}
