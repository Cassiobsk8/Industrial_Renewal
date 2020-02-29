package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static cassiokf.industrialrenewal.init.TileRegistration.MINING_TILE;

public class TileEntityMining extends TileEntity3x3MachineBase<TileEntityMining> implements ITickableTileEntity
{
    public CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack != null && stack.getFluid().equals(Fluids.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntityMining.this.Sync();
        }
    };
    public LazyOptional<IItemHandler> drillInv = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private int maxHeat = 18000;
    private int drillHeat;
    private int oldHeat;

    private int waterPtick = 10;
    private int energyPerTick = 1000;

    private boolean running;

    private Set<BlockPos> oresPos;

    //Client only
    private float rotation;
    private float ySlide = 0;
    private boolean revert;

    //Server
    private int particleTick;

    public TileEntityMining()
    {
        super(MINING_TILE.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(100000, 10240, 0)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityMining.this.Sync();
            }
        };
    }

    private IItemHandler createHandler()
    {
        return new ItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return stack.getItem() instanceof ItemDrill;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityMining.this.Sync();
            }
        };
    }

    @Override
    public void tick()
    {
        if (this.isMaster() && hasWorld())
        {
            running = canRun();
            doAnimation();
            if (!world.isRemote)
            {
                if (running)
                {
                    if (drillHeat < (waterTank.getFluidAmount() > waterPtick ? 9300 : 17300)) drillHeat += 20;
                } else
                {
                    drillHeat -= 30;
                }
                drillHeat = MathHelper.clamp(drillHeat, 3200, maxHeat);
            }
        }
    }

    private void doAnimation()
    {
        if (!world.isRemote)
        {
            if (running)
            {
                if (waterTank.getFluidAmount() > 0 && particleTick >= 10)
                {
                    particleTick = 0;
                    spawnFluidParticle(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f, Blocks.STONE);
                    spawnFluidParticle(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f, waterTank.getFluid().getFluid().getDefaultState().getBlockState().getBlock());
                }
                particleTick++;
            }
        } else
        {
            if (running)
            {
                rotation += 20f;
                if (rotation >= 360) rotation = 0;

                ySlide = Utils.lerp(ySlide, revert ? 0 : -1, 0.01f);
                if (ySlide >= -0.01 || ySlide <= -0.9) revert = !revert;
            } else
            {
                rotation = 0f;
                ySlide = Utils.lerp(ySlide, -1, 0.05f);
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityMining;
    }

    private void spawnFluidParticle(double x, double y, double z, Block block)
    {
        float f = (float) MathHelper.ceil(1.0F);
        double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
        int i = (int) (150.0D * d0);
        //((ServerWorld) world).addParticle(IParticleData., x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(block.getDefaultState()));
    }

    private boolean canRun()
    {
        Direction facing = getMasterFacing();
        BlockPos posPort = pos.offset(facing.rotateYCCW()).offset(facing.getOpposite()).down();
        return (world.isBlockPowered(posPort) || world.isBlockPowered(posPort.offset(facing.getOpposite())))
                && energyStorage.orElse(null).getEnergyStored() >= energyPerTick
                && !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, drillInv.orElse(null));
    }

    public String getWaterText(int line)
    {
        if (line == 1) return I18n.format("render.industrialrenewal.fluid") + ":";
        return Fluids.WATER.getDefaultState().getBlockState().getBlock().getNameTextComponent().getString();
    }

    public String getEnergyText(int line)
    {
        if (line == 1) return I18n.format("render.industrialrenewal.energy") + ":";
        return energyStorage.orElse(null).getEnergyStored() + " FE";
    }


    public String getHeatText()
    {
        String name = I18n.format("render.industrialrenewal.drillheat") + ": ";
        return name + (int) Utils.getConvertedTemperature(drillHeat / 100F) + Utils.getTemperatureUnit();
    }


    public float getWaterFill() //0 ~ 180
    {
        float currentAmount = waterTank.getFluidAmount() / 1000F;
        float totalCapacity = waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getEnergyFill() //0 ~ 180
    {
        float currentAmount = energyStorage.orElse(null).getEnergyStored() / 1000F;
        float totalCapacity = energyStorage.orElse(null).getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getHeatFill() //0 ~ 180
    {
        float currentAmount = drillHeat;
        float totalCapacity = maxHeat;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public boolean hasDrill()
    {
        return !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public ItemStack getDrill()
    {
        return drillInv.orElse(null).getStackInSlot(0);
    }

    public float getRotation()
    {
        return -rotation;
    }

    public float getSlide()
    {
        return ySlide;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT waterTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        compound.put("water", waterTag);
        drillInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        compound.putInt("heat", drillHeat);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT waterTag = compound.getCompound("water");
        this.waterTank.readFromNBT(waterTag);
        CompoundNBT invTag = compound.getCompound("inv");
        drillInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        this.drillHeat = compound.getInt("heat");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntityMining masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();

        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face).offset(face.rotateY())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> waterTank).cast();
        if (facing == Direction.UP && this.pos.equals(masterTE.getPos().offset(face.getOpposite()).up()) && capability == CapabilityEnergy.ENERGY)
            return masterTE.energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    public IItemHandler getDrillHandler()
    {
        return getMaster().drillInv.orElse(null);
    }
}
