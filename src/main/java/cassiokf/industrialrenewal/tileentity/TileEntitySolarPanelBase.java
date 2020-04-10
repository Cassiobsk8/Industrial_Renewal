package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntitySolarPanelBase extends TEBase implements ITickable
{
    public final VoltsEnergyContainer energyContainer;
    private int tick;
    private int random = 0;
    private int energyCanGenerate;

    public TileEntitySolarPanelBase()
    {
        this.energyContainer = new VoltsEnergyContainer(600, 0, 120)
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

            @Override
            public int receiveInternally(int maxReceive, boolean simulate)
            {
                return TileEntitySolarPanelBase.this.moveEnergyOut(maxReceive, simulate);
            }
        };
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        random = world.rand.nextInt(10);
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            if (tick >= (20 + random))
            {
                tick = 0;
                getEnergyFromSun();
            }
            tick++;

            if (energyCanGenerate > 0) energyContainer.receiveInternally(energyCanGenerate, false);
        }
    }

    public int moveEnergyOut(int energy, boolean simulate)
    {
        EnumFacing facing = EnumFacing.DOWN;
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        int out = 0;
        if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
        {
            final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
            if (consumer != null && consumer.canReceive())
            {
                out = consumer.receiveEnergy(energy, simulate);
            }
        }
        return out;
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
        if (world.isRaining()) normalize = normalize / 2;
        return Math.round(normalize * IRConfig.MainConfig.Main.baseSolarPanelMaxGeneration);
    }

    public void getEnergyFromSun()
    {
        if (world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(EnumFacing.UP))
                && world.getSkylightSubtracted() == 0 && this.energyContainer.getEnergyStored() != this.energyContainer.getMaxEnergyStored())
        {
            energyCanGenerate = getGeneration(world, pos);
        }
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
