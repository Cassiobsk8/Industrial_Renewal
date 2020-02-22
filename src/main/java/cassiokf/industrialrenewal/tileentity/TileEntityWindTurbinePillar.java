package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.init.TileRegistration.TURBINEPILLAR_TILE;

public class TileEntityWindTurbinePillar extends TileEntityMultiBlocksTube<TileEntityWindTurbinePillar> implements ICapabilityProvider
{
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);

    private float amount;//For Lerp

    private int tick;

    private Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};
    private BlockPos turbinePos;
    private boolean isBase;

    public TileEntityWindTurbinePillar()
    {
        super(TURBINEPILLAR_TILE.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(1024, 1024, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityWindTurbinePillar.this.markDirty();
            }
        };
    }

    private IEnergyStorage createEnergyDummy()
    {
        return new CustomEnergyStorage(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    @Override
    public void doTick()
    {
        if (isMaster())
        {
            if (!world.isRemote)
            {
                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(1024 * getPosSet().size(), thisEnergy.getEnergyStored())));
                int energyReceived = 0;
                for (BlockPos currentPos : getPosSet().keySet())
                {
                    TileEntity te = world.getTileEntity(currentPos);
                    Direction face = getPosSet().get(currentPos);
                    if (te != null)
                    {
                        IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                        if (eStorage != null && eStorage.canReceive())
                        {
                            energyReceived += eStorage.receiveEnergy(thisEnergy.extractEnergy(1024, true), false);
                            thisEnergy.extractEnergy(energyReceived, false);
                        }
                    }
                }

                outPut = energyReceived;

                if (oldOutPut != outPut)
                {
                    oldOutPut = outPut;
                    this.Sync();
                }
            } else if (getTurbinePos() != null && isBase)
            {
                if (tick % 10 == 0)
                {
                    tick = 0;
                    this.Sync();
                    if (!(world.getTileEntity(turbinePos) instanceof TileEntitySmallWindTurbine))
                    {
                        forceNewTurbinePos();
                    }
                }
                tick++;
            }
        }
    }

    @Override
    public Direction[] getFacesToCheck()
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
        if (isBase) forceNewTurbinePos();
        if (world.isRemote) return;
        for (Direction face : Direction.Plane.HORIZONTAL)
        {
            BlockPos currentPos = pos.offset(face);
            if (isBase)
            {
                BlockState state = world.getBlockState(currentPos);
                TileEntity te = world.getTileEntity(currentPos);
                boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar)
                        && te != null && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();

                if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                    else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
            } else
            {
                if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
            }
        }
        this.Sync();
    }

    private BlockPos getTurbinePos()
    {
        if (turbinePos != null) return turbinePos;
        return forceNewTurbinePos();
    }


    private BlockPos forceNewTurbinePos()
    {
        int n = 1;
        while (world.getTileEntity(pos.up(n)) instanceof TileEntityWindTurbinePillar)
        {
            n++;
        }
        if (world.getTileEntity(pos.up(n)) instanceof TileEntitySmallWindTurbine) turbinePos = pos.up(n);
        else turbinePos = null;
        return turbinePos;
    }

    public Direction getBlockFacing()
    {
        return getBlockState().get(BlockWindTurbinePillar.FACING);
    }

    public float getGenerationforGauge()
    {
        float currentAmount = getEnergyGenerated();
        float totalCapacity = TileEntitySmallWindTurbine.getMaxGeneration();
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public int getEnergyGenerated()
    {
        return getMaster().outPut;
    }

    public String getText()
    {
        if (getMaster() == null || getMaster().getTurbinePos() == null) return "No Turbine";
        return getEnergyGenerated() + " FE/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        BlockState state = world.getBlockState(pos.down());
        return !(state.getBlock() instanceof BlockWindTurbinePillar);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && (facing == Direction.UP))
            return getMaster().energyStorage.cast();
        if (capability == CapabilityEnergy.ENERGY && (isBase()))
            return dummyEnergy.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        this.isBase = compound.getBoolean("base");
        TileEntityWindTurbinePillar te = null;
        if (compound.contains("masterPos") && hasWorld())
            te = (TileEntityWindTurbinePillar) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
        if (te != null) this.setMaster(te);
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        compound.putBoolean("base", this.isBase);
        if (getMaster() != null) compound.putLong("masterPos", getMaster().getPos().toLong());
        return super.write(compound);
    }
}
