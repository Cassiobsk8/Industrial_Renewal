package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipe;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class TileEntityFluidPipeBase extends TileEntityMultiBlocksTube<TileEntityFluidPipeBase> implements ICapabilityProvider
{
    public CustomFluidTank tank = new CustomFluidTank(FluidAttributes.BUCKET_VOLUME)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityFluidPipeBase.this.markDirty();
        }
    };

    public int maxOutput = IRConfig.Main.maxFluidPipeTransferAmount.get();

    public TileEntityFluidPipeBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void doTick()
    {
        if (hasWorld() && !world.isRemote && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            tank.setCapacity(Math.max(maxOutput * quantity, tank.getFluidAmount()));

            if (quantity > 0)
            {
                int canAccept = moveFluid(IFluidHandler.FluidAction.SIMULATE, 1, mapPosSet);
                outPut = canAccept > 0 ? moveFluid(IFluidHandler.FluidAction.EXECUTE, canAccept, mapPosSet) : 0;
            } else outPut = 0;

            outPutCount = mapPosSet.size();
            if ((oldOutPut != outPut) || (oldOutPutCount != outPutCount))
            {
                oldOutPut = outPut;
                oldOutPutCount = outPutCount;
                this.Sync();
            }
        }
    }

    public int moveFluid(IFluidHandler.FluidAction action, int validOutputs, Map<BlockPos, Direction> mapPosSet)
    {
        int canAccept = 0;
        int out = 0;
        int realMaxOutput = Math.min(tank.getFluidAmount() / validOutputs, maxOutput);
        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            Direction face = mapPosSet.get(posM).getOpposite();
            if (te != null)
            {
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).orElse(null);
                if (tankStorage != null
                        && tankStorage.isFluidValid(0, tank.getFluid())
                        && tank.drain(maxOutput, IFluidHandler.FluidAction.SIMULATE) != null)
                {
                    int fluid = tankStorage.fill(
                            tank.drain(realMaxOutput, IFluidHandler.FluidAction.SIMULATE),
                            action == IFluidHandler.FluidAction.SIMULATE ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
                    if (action == IFluidHandler.FluidAction.SIMULATE)
                    {
                        if (fluid > 0) canAccept++;
                    } else
                    {
                        out += fluid;
                        tank.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
        return action == IFluidHandler.FluidAction.SIMULATE ? canAccept : out;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (!world.isRemote)
        {
            for (Direction face : Direction.values())
            {
                BlockPos currentPos = pos.offset(face);
                TileEntity te = world.getTileEntity(currentPos);
                boolean hasMachine = te != null
                        && !(te instanceof TileEntityFluidPipeBase)
                        && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).isPresent();
                if (hasMachine)
                {
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                } else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityFluidPipeBase || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMaster() != null)
            return LazyOptional.of(() -> getMaster().tank).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound)
    {
        CompoundNBT tag = new CompoundNBT();
        tank.writeToNBT(tag);
        tagCompound.put("fluid", tag);
        return super.write(tagCompound);
    }

    @Override
    public void read(CompoundNBT tagCompound)
    {
        CompoundNBT tag = tagCompound.getCompound("fluid");
        tank.readFromNBT(tag);
        super.read(tagCompound);
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(MASTER, IRConfig.Main.showMaster.get() && isMaster())
                .withInitial(SOUTH, canConnectToPipe(Direction.SOUTH))
                .withInitial(NORTH, canConnectToPipe(Direction.NORTH))
                .withInitial(EAST, canConnectToPipe(Direction.EAST))
                .withInitial(WEST, canConnectToPipe(Direction.WEST))
                .withInitial(UP, canConnectToPipe(Direction.UP))
                .withInitial(DOWN, canConnectToPipe(Direction.DOWN))
                .withInitial(CSOUTH, canConnectToCapability(Direction.SOUTH))
                .withInitial(CNORTH, canConnectToCapability(Direction.NORTH))
                .withInitial(CEAST, canConnectToCapability(Direction.EAST))
                .withInitial(CWEST, canConnectToCapability(Direction.WEST))
                .withInitial(CUP, canConnectToCapability(Direction.UP))
                .withInitial(CDOWN, canConnectToCapability(Direction.DOWN))
                .build();
    }

    public boolean canConnectToPipe(Direction neighborDirection)
    {
        BlockPos neighborPos = pos.offset(neighborDirection);
        TileEntity te = world.getTileEntity(neighborPos);
        return instanceOf(te);
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos offset = pos.offset(neighborDirection);
        BlockState state = world.getBlockState(offset);
        TileEntity te = world.getTileEntity(offset);
        return !(state.getBlock() instanceof BlockFluidPipe)
                && te != null
                && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighborDirection.getOpposite()).isPresent();
    }
}
