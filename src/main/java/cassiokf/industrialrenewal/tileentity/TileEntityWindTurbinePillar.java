package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCable;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class TileEntityWindTurbinePillar extends TileEntityEnergyCable
{
    private final VoltsEnergyContainer dummyEnergyContainer;

    private float amount;//For Lerp
    private int oldOutPut = -1;

    private EnumFacing facing;

    private final EnumFacing[] faces = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
    private boolean isBase;

    public TileEntityWindTurbinePillar()
    {
        this.dummyEnergyContainer = new VoltsEnergyContainer(0, 0, 0)
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
        };
    }

    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.MainConfig.Main.maxEnergySWindTurbine;
    }

    @Override
    public void beforeInitialize()
    {
        isBase = getIsBase();
    }

    @Override
    public EnumFacing[] getFacesToCheck()
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
        outPut = 0;
        potentialEnergy = 0;
        if (world.isRemote || !isBase) return;
        for (EnumFacing face : EnumFacing.HORIZONTALS)
        {
            BlockPos currentPos = pos.offset(face);

            IBlockState state = world.getBlockState(currentPos);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar)
                    && te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite());

            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).canReceive())
                addMachine(currentPos, face);
        }
        this.Sync();
    }

    public EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockWindTurbinePillar
                ? state.getValue(BlockWindTurbinePillar.FACING) : EnumFacing.NORTH;
        return facing;
    }

    public float getGenerationForGauge()
    {
        float currentAmount = Utils.normalize(getMaster().averageEnergy, 0, getMaxEnergyToTransport());
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public float getPotentialValue()
    {
        float currentAmount = Utils.normalize(getMaster().potentialEnergy, 0, getMaxEnergyToTransport());
        return currentAmount * 90f;
    }

    public String getText()
    {
        return getMaster().averageEnergy + " FE/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        IBlockState state = world.getBlockState(pos.down());
        return !(state.getBlock() instanceof BlockWindTurbinePillar);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.UP || isBase())) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.UP))
            return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
        if (capability == CapabilityEnergy.ENERGY && (isBase()))
            return CapabilityEnergy.ENERGY.cast(dummyEnergyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.isBase = compound.getBoolean("base");
        if (hasWorld() && world.isRemote) this.potentialEnergy = compound.getInteger("potential");
        if (hasWorld() && world.isRemote) this.outPut = compound.getInteger("outPut");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("base", this.isBase);
        compound.setInteger("potential", potentialEnergy);
        compound.setInteger("outPut", outPut);
        return super.writeToNBT(compound);
    }
}
