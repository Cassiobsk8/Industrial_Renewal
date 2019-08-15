package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockConcrete;
import cassiokf.industrialrenewal.blocks.BlockDamIntake;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityDamIntake extends TileEntity implements ITickable
{
    public FluidTank tank = new FluidTank(10000)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntityDamIntake.this.markDirty();
        }
    };
    int waterAmount = -1;
    List<BlockPos> connectedWalls = new ArrayList<BlockPos>();

    @Override
    public void onLoad()
    {
        initializeMultiblockIfNecessary();
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            if (tank.getFluidAmount() < tank.getCapacity())
            {
                FluidStack pressurizedWaterStack = new FluidStack(FluidRegistry.WATER, waterAmount * 5);
                tank.fillInternal(pressurizedWaterStack, true);
            }
            if (tank.getFluidAmount() > 0)
            {
                EnumFacing facing = getBlockFacing();
                TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));
                if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
                {
                    IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                    if (handler != null && handler.getTankProperties()[0].canFill())
                    {
                        tank.drain(handler.fill(tank.drain(5000, false), true), true);
                    }
                }
            }
        }
    }

    public int getWaterAmount()
    {
        initializeMultiblockIfNecessary();
        return waterAmount;
    }

    private void initializeMultiblockIfNecessary()
    {
        if (waterAmount < 0)
        {
            connectedWalls.clear();
            connectedWalls.add(pos);
            int n = 1;
            while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockConcrete)
            {
                n++;
            }
            EnumFacing facing = getBlockFacing();
            for (int i = -6; i <= 6; i++)
            {
                for (int y = 0; y <= n; y++)
                {
                    BlockPos cPos = (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) ?
                            (new BlockPos(pos.getX() + i, pos.getY() + y, pos.getZ())) :
                            (new BlockPos(pos.getX(), pos.getY() + y, pos.getZ() + i));
                    TileEntity te = world.getTileEntity(cPos);
                    if (te instanceof TileEntityConcrete && !((TileEntityConcrete) te).isUsed())
                    {
                        connectedWalls.add(cPos);
                        ((TileEntityConcrete) te).setUsed(true);
                    }
                }
            }
            waterAmount = 0;
            for (BlockPos wall : connectedWalls)
            {
                int f = 1;
                while (world.getBlockState(wall.offset(facing, f)).getMaterial() == Material.WATER
                        && world.getBlockState(wall.offset(facing, f)).getValue(BlockFluidBase.LEVEL) == 0
                        && f <= 10)
                {
                    f++;
                }
                waterAmount += f - 1;
            }
            waterAmount = MathHelper.clamp(waterAmount, 0, 1000);
        }
    }

    @Override
    public void invalidate()
    {
        for (BlockPos wall : connectedWalls)
        {
            TileEntityConcrete te = (TileEntityConcrete) world.getTileEntity(wall);
            if (te != null) te.setUsed(false);
        }
        super.invalidate();
    }

    private EnumFacing getBlockFacing()
    {
        return world.getBlockState(pos).getValue(BlockDamIntake.FACING);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == getBlockFacing().getOpposite();
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == getBlockFacing().getOpposite())
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        return super.getCapability(capability, facing);
    }
}
