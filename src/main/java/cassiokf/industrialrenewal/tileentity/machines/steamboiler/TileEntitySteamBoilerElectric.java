package cassiokf.industrialrenewal.tileentity.machines.steamboiler;

import cassiokf.industrialrenewal.init.FluidInit;
import cassiokf.industrialrenewal.tileentity.Fluid.TileFluidHandlerBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntitySteamBoilerElectric extends TileFluidHandlerBase implements ICapabilityProvider, ITickable
{
    private final VoltsEnergyContainer energyContainer;
    public FluidTank waterTank = new FluidTank(32000)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid.getFluid().equals(FluidRegistry.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoilerElectric.this.Sync();
        }
    };
    public FluidTank steamTank = new FluidTank(320000)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoilerElectric.this.Sync();
        }
    };

    private boolean master;
    private int maxHeat = 32000;
    private int heat;
    private int energyPerTick = 24;
    private int oldHeat;

    public TileEntitySteamBoilerElectric()
    {
        this.energyContainer = new VoltsEnergyContainer(10000, 2000, 0)
        {
            @Override
            public void onEnergyChange()
            {
                if (!world.isRemote) TileEntitySteamBoilerElectric.this.Sync();
            }
        };
    }

    @Override
    public void update()
    {
        if (this.getIsMaster() && !this.world.isRemote)
        {
            if (this.energyContainer.getEnergyStored() > 0)
            {
                heat += 10;
                this.energyContainer.setEnergyStored(Math.max(0, this.energyContainer.getEnergyStored() - energyPerTick));
            }
            if (heat >= 10000 && this.waterTank.getFluidAmount() >= 100 && this.steamTank.getFluidAmount() < this.steamTank.getCapacity())
            {
                this.waterTank.drain(100, true);
                FluidStack steamStack = new FluidStack(FluidInit.STEAM, 1000);
                this.steamTank.fillInternal(steamStack, true);
                heat -= 2;
            }
            heat -= 2;
            heat = MathHelper.clamp(heat, 0, maxHeat);
            TileEntity upTE = this.world.getTileEntity(pos.up(2));
            if (this.steamTank.getFluidAmount() > 0 && upTE != null && upTE.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IFluidHandler upTank = upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
                this.steamTank.drain(upTank.fill(this.steamTank.drain(1000, false), true), true);
            }
            if (this.steamTank.getFluidAmount() > 0 && heat < 9000)
            {
                this.steamTank.drain(10, true);
            }
            if (oldHeat != heat)
            {
                this.Sync();
                oldHeat = heat;
            }
        }
    }

    @Override
    public void onLoad()
    {
        this.getIsMaster();
    }

    public TileEntitySteamBoilerElectric getMaster()
    {
        List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            Block block = world.getBlockState(currentPos).getBlock();
            if (block instanceof BlockSteamBoilerElectric && ((TileEntitySteamBoilerElectric) world.getTileEntity(currentPos)).getIsMaster())
            {
                return ((TileEntitySteamBoilerElectric) world.getTileEntity(currentPos));
            }
        }
        return null;
    }

    public void breakMultiBlocks()
    {
        if (!this.getIsMaster())
        {
            if (getMaster() != null)
            {
                getMaster().breakMultiBlocks();
            }
            return;
        }
        List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            Block block = world.getBlockState(currentPos).getBlock();
            if (block instanceof BlockSteamBoilerElectric) world.setBlockToAir(currentPos);
        }
    }

    public boolean isMaster()
    {
        return this.master;
    }

    private boolean getIsMaster()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof BlockSteamBoilerElectric)) return false;
        return state.getValue(BlockSteamBoilerElectric.MASTER);
    }

    public EnumFacing getBlockFacing()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockSteamBoilerElectric.FACING);
    }

    public String getWaterText()
    {
        return FluidRegistry.WATER.getName();
    }

    public String getSteamText()
    {
        return FluidInit.STEAM.getName();
    }

    public String getEnergyText()
    {
        int energy = this.energyContainer.getEnergyStored();
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = energy / 1000 + "K FE";
        if (energy >= 1000000)
            text = energy / 1000000 + "M FE";
        return text;
    }

    public String getHeatText()
    {
        return heat / 100 + " ºC";
    }

    public float GetEnergyFill() //0 ~ 180
    {
        float currentAmount = this.energyContainer.getEnergyStored() / 1000;
        float totalCapacity = this.energyContainer.getMaxEnergyStored() / 1000;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float GetWaterFill() //0 ~ 180
    {
        float currentAmount = this.waterTank.getFluidAmount() / 1000;
        float totalCapacity = this.waterTank.getCapacity() / 1000;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float GetSteamFill() //0 ~ 180
    {
        float currentAmount = this.steamTank.getFluidAmount() / 1000;
        float totalCapacity = this.steamTank.getCapacity() / 1000;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getHeatFill() //0 ~ 180
    {
        float currentAmount = heat;
        float totalCapacity = maxHeat;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 140f;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = new NBTTagCompound();
        NBTTagCompound steamTag = new NBTTagCompound();
        this.waterTank.writeToNBT(waterTag);
        this.steamTank.writeToNBT(steamTag);
        compound.setTag("water", waterTag);
        compound.setTag("steam", steamTag);
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        compound.setBoolean("master", this.getIsMaster());
        compound.setInteger("heat", heat);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = compound.getCompoundTag("water");
        NBTTagCompound steamTag = compound.getCompoundTag("steam");
        this.waterTank.readFromNBT(waterTag);
        this.steamTank.readFromNBT(steamTag);
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        this.master = compound.getBoolean("master");
        this.heat = compound.getInteger("heat");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        EnumFacing face = this.getBlockFacing();
        TileEntitySteamBoilerElectric masterTE = this.getMaster();
        if (masterTE == null) return false;

        return (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face && this.pos.equals(masterTE.getPos().down().offset(this.getBlockFacing())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face.getOpposite() && this.pos.equals(masterTE.getPos().down().offset(this.getBlockFacing().getOpposite())) && capability == CapabilityEnergy.ENERGY);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        EnumFacing face = this.getBlockFacing();
        TileEntitySteamBoilerElectric masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);

        if (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.steamTank);
        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(this.getBlockFacing())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.waterTank);
        if (facing == face.getOpposite() && this.pos.equals(masterTE.getPos().down().offset(this.getBlockFacing().getOpposite())) && capability == CapabilityEnergy.ENERGY)
            return (T) masterTE.energyContainer;

        return super.getCapability(capability, facing);
    }
}
