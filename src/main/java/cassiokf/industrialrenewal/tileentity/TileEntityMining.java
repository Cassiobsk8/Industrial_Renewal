package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class TileEntityMining extends TileEntityMultiBlockBase<TileEntityMining>
{
    private final VoltsEnergyContainer energyContainer;
    public FluidTank waterTank = new FluidTank(32000)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid != null && fluid.getFluid().equals(FluidRegistry.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntityMining.this.sync();
        }
    };
    public ItemStackHandler drillInv = new ItemStackHandler(1)
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
            TileEntityMining.this.sync();
        }
    };
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
        this.energyContainer = new VoltsEnergyContainer(100000, 10240, 10240)
        {
            @Override
            public boolean canExtract()
            {
                return false;
            }

            @Override
            public void onEnergyChange()
            {
                TileEntityMining.this.sync();
            }
        };
    }

    @Override
    public void tick()
    {
        if (this.isMaster())
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
                    spawnFluidParticle(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f, waterTank.getFluid().getFluid().getBlock());
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
        ((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(block.getDefaultState()));
    }

    private boolean canRun()
    {
        EnumFacing facing = getMasterFacing();
        BlockPos posPort = pos.offset(facing.rotateYCCW()).offset(facing.getOpposite()).down();
        return (world.isBlockPowered(posPort) || world.isBlockPowered(posPort.offset(facing.getOpposite())))
                && energyContainer.getEnergyStored() >= energyPerTick
                && !drillInv.getStackInSlot(0).isEmpty();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, drillInv);
    }

    public String getWaterText(int line)
    {
        if (line == 1) return I18n.format("render.industrialrenewal.fluid") + ":";
        return FluidRegistry.WATER.getName();
    }

    public String getEnergyText(int line)
    {
        if (line == 1) return I18n.format("render.industrialrenewal.energy") + ":";
        return energyContainer.getEnergyStored() + " FE";
    }


    public String getHeatText()
    {
        String st;
        switch (IRConfig.MainConfig.Main.temperatureScale)
        {
            default:
            case 0:
                st = " ºC";
                break;
            case 1:
                st = " ºF";
                break;
            case 2:
                st = " K";
                break;
        }
        String name = I18n.format("render.industrialrenewal.drillheat") + ": ";
        return name + (int) Utils.getConvertedTemperature(drillHeat / 100F) + st;
    }


    public float getWaterFill() //0 ~ 180
    {
        float currentAmount = this.waterTank.getFluidAmount() / 1000F;
        float totalCapacity = this.waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getEnergyFill() //0 ~ 180
    {
        float currentAmount = this.energyContainer.getEnergyStored() / 1000F;
        float totalCapacity = this.energyContainer.getMaxEnergyStored() / 1000F;
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
        return !drillInv.getStackInSlot(0).isEmpty();
    }

    public ItemStack getDrill()
    {
        return drillInv.getStackInSlot(0);
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
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = new NBTTagCompound();
        this.waterTank.writeToNBT(waterTag);
        compound.setTag("water", waterTag);
        compound.setTag("firebox", this.drillInv.serializeNBT());
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        compound.setInteger("heat", drillHeat);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = compound.getCompoundTag("water");
        this.waterTank.readFromNBT(waterTag);
        this.drillInv.deserializeNBT(compound.getCompoundTag("firebox"));
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        this.drillHeat = compound.getInteger("heat");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        TileEntityMining masterTE = this.getMaster();
        if (masterTE == null) return false;
        EnumFacing face = masterTE.getMasterFacing();
        return (facing == face && this.pos.equals(masterTE.getPos().down().offset(face).offset(face.rotateY())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().offset(face.getOpposite()).up()) && capability == CapabilityEnergy.ENERGY);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        TileEntityMining masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        EnumFacing face = masterTE.getMasterFacing();

        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face).offset(face.rotateY())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.waterTank);
        if (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().offset(face.getOpposite()).up()) && capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(masterTE.energyContainer);
        return super.getCapability(capability, facing);
    }

    public IItemHandler getDrillHandler()
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.getMaster().drillInv);
    }
}
