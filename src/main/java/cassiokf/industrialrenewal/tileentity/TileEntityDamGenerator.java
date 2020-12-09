package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityDamGenerator extends TileEntityMultiBlockBase<TileEntityDamGenerator> implements IMecanicalEnergy, IDynamicSound
{
    public static final int maxGeneration = 1024;
    private static final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(0, 0, 0)
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
    private static final float volume = IRConfig.MainConfig.Sounds.turbineVolume * IRConfig.MainConfig.Sounds.masterVolumeMult;
    private int oldGeneration;
    private int generation;
    private int rotation;

    @Override
    public void tick()
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
                        IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
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
    public boolean canAcceptRotation(BlockPos pos, EnumFacing side)
    {
        return pos.equals(getMaster().getPos().down()) && side == EnumFacing.DOWN;
    }

    @Override
    public int passRotation(int amount)
    {
        if (isInvalid() || getMaster().isInvalid()) return 0;
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
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (facing == EnumFacing.UP
                && pos.equals(getMaster().getPos().up())
                && capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(energyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("generation", generation);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        generation = compound.getInteger("generation");
        super.readFromNBT(compound);
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
