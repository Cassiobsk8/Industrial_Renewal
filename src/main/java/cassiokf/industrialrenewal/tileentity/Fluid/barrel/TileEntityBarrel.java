package cassiokf.industrialrenewal.tileentity.Fluid.barrel;

import cassiokf.industrialrenewal.Registry.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketBarrel;
import cassiokf.industrialrenewal.network.PacketReturnBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class TileEntityBarrel extends TileFluidHandler
{
    private int prevAmount;
    public FluidTank tank = new FluidTank(64000)
    {
        @Override
        protected void onContentsChanged()
        {
            if (!world.isRemote)
            {
                TileEntityBarrel.this.save();
                //NetworkHandler.INSTANCE.sendToAllAround(new PacketBarrel(TileEntityBarrel.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16));
            }
        }
    };

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void onLoad()
    {
        if (world.isRemote)
        {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnBarrel(this));
        }
    }

    public void save() {
        this.markDirty();
        if (Math.abs(prevAmount - this.tank.getFluidAmount()) >= 1000) {
            prevAmount = this.tank.getFluidAmount();
            if (!world.isRemote) {
                NetworkHandler.INSTANCE.sendToAllAround(new PacketBarrel(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64));
            }
        }
    }

    public String GetChatQuantity()
    {
        if (this.tank.getFluid() != null)
        {
            return this.tank.getFluid().getLocalizedName() + ": " + this.tank.getFluidAmount() + " mB";
        } else
        {
            return "Empty";
        }
    }

    public void readTankFromNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Empty"))
        {
            tag.removeTag("Empty");
        }
        tank.readFromNBT(tag);
    }

    private void writeEntityTankToNBT(NBTTagCompound tag)
    {
        tank.writeToNBT(tag);
    }

    public NBTTagCompound GetTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeEntityTankToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        compound.setTag("fluid", tag);

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = compound.getCompoundTag("fluid");
        tank.readFromNBT(tag);

        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }
}
