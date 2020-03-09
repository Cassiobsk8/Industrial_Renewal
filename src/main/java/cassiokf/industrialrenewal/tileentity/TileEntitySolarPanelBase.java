package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSolarPanel;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntitySolarPanelBase extends TileEntity implements ICapabilityProvider, ITickable
{
    public final VoltsEnergyContainer energyContainer;

    public TileEntitySolarPanelBase()
    {
        this.energyContainer = new VoltsEnergyContainer(600, 0, 120)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySolarPanelBase.this.markDirty();
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
        if (this.hasWorld() && !this.world.isRemote)
        {
            getEnergyFromSun();
            int energy = this.energyContainer.getEnergyStored();
            updatePanel(EnumFacing.DOWN, energy);
        }
    }

    public static int getGeneration(World world, BlockPos pos)
    {
        int i = world.getLightFor(EnumSkyBlock.SKY, pos) - world.getSkylightSubtracted();
        float f = world.getCelestialAngleRadians(1.0F);
        if (i > 0)
        {
            float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
            f = f + (f1 - f) * 0.2F;
            i = Math.round((float) i * MathHelper.cos(f));
        }
        i = MathHelper.clamp(i, 0, 15);
        float normalize = i / 15f;
        return Math.round(normalize * IRConfig.MainConfig.Main.baseSolarPanelMaxGeneration);
    }

    public void getEnergyFromSun()
    {
        if (world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(EnumFacing.UP))
                && world.getSkylightSubtracted() == 0 && this.energyContainer.getEnergyStored() != this.energyContainer.getMaxEnergyStored())
        {
            int result = this.energyContainer.getEnergyStored() + getGeneration(world, pos);
            if (result > this.energyContainer.getMaxEnergyStored())
            {
                result = this.energyContainer.getMaxEnergyStored();
            }
            this.energyContainer.setEnergyStored(result);
        }
    }

    public void updatePanel(EnumFacing facing, int energy)
    {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (tileEntity != null && !tileEntity.isInvalid())
        {
            if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()) && !(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockSolarPanel))
            {
                final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                if (consumer != null && consumer.canReceive())
                {
                    this.energyContainer.extractEnergy(consumer.receiveEnergy(energy, false), false);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.DOWN) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.DOWN)
            return CapabilityEnergy.ENERGY.cast(this.energyContainer);
        return super.getCapability(capability, facing);
    }
}
