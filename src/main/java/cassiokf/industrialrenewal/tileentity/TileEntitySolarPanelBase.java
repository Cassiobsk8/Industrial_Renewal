package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntitySolarPanelBase extends TEBase implements ITickableTileEntity
{
    public final CustomEnergyStorage energyContainer = new CustomEnergyStorage(600, 120, 120)
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
    private int tick;
    private final int random;
    private int energyCanGenerate;

    public TileEntitySolarPanelBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        random = ThreadLocalRandom.current().nextInt(10);
    }

    @Override
    public void tick()
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
        Direction facing = Direction.DOWN;
        final TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        int out = 0;
        if (tileEntity != null)
        {
            final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
            if (consumer != null && consumer.canReceive())
            {
                out = consumer.receiveEnergy(energy, simulate);
            }
        }
        return out;
    }

    public static int getGeneration(World world, BlockPos pos)
    {
        int i = world.getLightFor(LightType.SKY, pos) - world.getSkylightSubtracted();
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
        return Math.round(normalize * IRConfig.Main.baseSolarPanelMaxGeneration.get());
    }

    public void getEnergyFromSun()
    {
        if (world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(Direction.UP))
                && world.getSkylightSubtracted() == 0 && this.energyContainer.getEnergyStored() != this.energyContainer.getMaxEnergyStored())
        {
            energyCanGenerate = getGeneration(world, pos);
        }
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == Direction.DOWN)
            return LazyOptional.of(() -> energyContainer).cast();
        return super.getCapability(capability, facing);
    }
}
