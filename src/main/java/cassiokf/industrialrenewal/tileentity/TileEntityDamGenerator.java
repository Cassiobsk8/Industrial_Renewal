package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityDamGenerator extends TileEntityMultiBlockBase<TileEntityDamGenerator> implements IMecanicalEnergy, IDynamicSound
{
    public static final int maxGeneration = 1024;
    private static final CustomEnergyStorage energyContainer = new CustomEnergyStorage(0, 0, 0)
    {
        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public boolean canReceive()
        {
            return false;
        }
    };
    private static final float volume = IRConfig.Sounds.turbineVolume.get() * IRConfig.Sounds.masterVolumeMult.get();
    private int oldGeneration;
    private int generation;
    private int rotation;

    public TileEntityDamGenerator(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
    {
        if (isMaster())
        {
            if (!world.isRemote)
            {
                generation = (int) (Utils.normalize(rotation, 0, 6000) * maxGeneration);
                if (generation > 0)
                {
                    TileEntity te = world.getTileEntity(pos.up(2));
                    if (te != null)
                    {
                        IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).orElse(null);
                        if (energyStorage != null)
                        {
                            energyStorage.receiveEnergy(generation, false);
                        }
                    }
                }

                if (generation != oldGeneration)
                {
                    oldGeneration = generation;
                    sync();
                }
                rotation = 0;
            } else
            {
                updateSound(getPitch());
            }
        }
    }

    private void updateSound(float pitch)
    {
        if (!world.isRemote) return;
        if (this.generation > 0)
        {
            IRSoundHandler.playRepeatableSound(IRSoundRegister.MOTOR_ROTATION_RESOURCEL, volume, pitch, pos);
        } else
        {
            IRSoundHandler.stopTileSound(pos);
        }
    }

    @Override
    public void onMasterBreak()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
    }

    @Override
    public float getPitch()
    {
        return Utils.normalize(generation, 0, maxGeneration) * 1.4f;
    }

    @Override
    public float getVolume()
    {
        return volume;
    }

    @Override
    public boolean canAcceptRotation(BlockPos pos, Direction side)
    {
        return pos.equals(getMaster().getPos().down()) && side == Direction.DOWN;
    }

    @Override
    public int passRotation(int amount)
    {
        if (isRemoved() || getMaster().isRemoved()) return 0;
        if (!isMaster()) return getMaster().passRotation(amount);
        rotation = amount;
        return amount;
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityDamGenerator;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (facing == Direction.UP
                && pos.equals(getMaster().getPos().up())
                && capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyContainer).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putInt("generation", generation);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        generation = compound.getInt("generation");
        super.read(compound);
    }

    public String getGenerationText()
    {
        return Utils.formatEnergyString(generation) + "/t";
    }

    public float getGenerationFill()
    {
        return Utils.normalize(generation, 0, maxGeneration) * 90;
    }
}
