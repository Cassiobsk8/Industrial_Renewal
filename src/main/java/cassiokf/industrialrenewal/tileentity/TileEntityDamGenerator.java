package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityDamGenerator extends TileEntity3x3MachineBase<TileEntityDamGenerator> implements IMecanicalEnergy, ITickable, IDynamicSound
{
    public static int maxGeneration = 1024;
    private final VoltsEnergyContainer energyContainer;
    private float volume = IRConfig.MainConfig.Sounds.TurbineVolume;
    private int oldGeneration;
    private int generation;
    private int rotation;

    public TileEntityDamGenerator()
    {
        this.energyContainer = new VoltsEnergyContainer(0, 0, 0)
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
    }

    @Override
    public void update()
    {
        if (isMaster())
        {
            if (!world.isRemote)
            {
                generation = (int) (Utils.normalize(rotation, 0, 6000) * maxGeneration);
                if (generation > 0)
                {
                    TileEntity te = world.getTileEntity(pos.up(2));
                    if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN))
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
                    Sync();
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
    public int passRotation(int amount)
    {
        if (!isMaster()) return getMaster().passRotation(amount);
        rotation = amount;
        return amount;
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x2x3CenteredPlus1OnTop(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityDamGenerator;
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return facing == EnumFacing.UP
                && this.pos.equals(getMaster().getPos().up())
                && capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (facing == EnumFacing.UP
                && this.pos.equals(getMaster().getPos().up())
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
