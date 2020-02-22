package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockConcrete;
import cassiokf.industrialrenewal.blocks.BlockDamIntake;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cassiokf.industrialrenewal.init.TileRegistration.DAMINTAKE_TILE;

public class TileEntityDamIntake extends TileEntity implements ITickableTileEntity
{
    public CustomFluidTank tank = new CustomFluidTank(10000)
    {

        @Override
        protected void onContentsChanged()
        {
            TileEntityDamIntake.this.markDirty();
        }
    };

    int waterAmount = -1;
    boolean initialized = false;
    List<BlockPos> connectedWalls = new ArrayList<BlockPos>();

    public TileEntityDamIntake()
    {
        super(DAMINTAKE_TILE.get());
    }

    //@Override
    //public void onLoad()
    //{
    //    initializeMultiblockIfNecessary();
    //}

    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            if (!initialized) initializeMultiblockIfNecessary();
            if (tank.getFluidAmount() < tank.getCapacity())
            {
                FluidStack pressurizedWaterStack = new FluidStack(Fluids.WATER, waterAmount * 5);
                tank.fill(pressurizedWaterStack, IFluidHandler.FluidAction.EXECUTE);
            }
            if (tank.getFluidAmount() > 0)
            {
                Direction facing = getBlockFacing();
                TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));
                if (te != null)
                {
                    IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                    if (handler != null)
                    {
                        tank.drain(handler.fill(tank.drain(5000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
            Direction facing = getBlockFacing();
            for (int i = -6; i <= 6; i++)
            {
                for (int y = 0; y <= n; y++)
                {
                    BlockPos cPos = (facing == Direction.NORTH || facing == Direction.SOUTH) ?
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
                        && world.getBlockState(wall.offset(facing, f)).get(FlowingFluidBlock.LEVEL) == 0
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
    public void remove()
    {
        for (BlockPos wall : connectedWalls)
        {
            TileEntity te = world.getTileEntity(wall);
            if (te instanceof TileEntityConcrete) ((TileEntityConcrete) te).setUsed(false);
        }
        super.remove();
    }

    private Direction getBlockFacing()
    {
        return getBlockState().get(BlockDamIntake.FACING);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == getBlockFacing().getOpposite())
            return LazyOptional.of(() -> tank).cast();
        return super.getCapability(capability, facing);
    }
}
