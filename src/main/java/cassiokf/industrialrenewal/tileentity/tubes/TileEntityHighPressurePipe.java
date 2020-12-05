package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;


import java.util.Map;

public class TileEntityHighPressurePipe extends TileEntityMultiBlocksTube<TileEntityHighPressurePipe> implements ICompressedFluidCapability
{
    public int maxOutput = References.BUCKET_VOLUME;
    private boolean inUse = false;

    public TileEntityHighPressurePipe(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
    {
        if (!world.isRemote && isMaster())
        {
            limitedOutPutMap.clear();
        }
    }

    @Override
    public boolean canAccept(Direction face, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean canPipeConnect(Direction face, BlockPos pos)
    {
        return false;
    }

    @Override
    public int passCompressedFluid(int amount, int y, boolean simulate)
    {
        if (!isMaster() && !isMasterInvalid()) return getMaster().passCompressedFluid(amount, y, simulate);
        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;

        if (amount <= 0) return 0;
        int out = 0;
        final Map<TileEntity, Direction> mapPosSet = getMachineContainers();
        int quantity = getRealQuantity(mapPosSet);

        if (quantity > 0)
        {
            out = moveFluid(amount, y, simulate, mapPosSet);
            if (!simulate) outPut += out;
        }
        outPutCount = quantity;

        inUse = false;
        return out;
    }

    private int getRealQuantity(Map<TileEntity, Direction> mapPosSet)
    {
        int out = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (te != null && !te.isRemoved()) out++;
            else mapPosSet.remove(te);
        }
        return out;
    }

    public int moveFluid(int amount, int y, boolean simulate, Map<TileEntity, Direction> mapPosSet)
    {
        int out = 0;
        int validOutputs = getMaxOutput(mapPosSet, amount, y);
        if (validOutputs == 0) return 0;
        int realMaxOutput = Math.min(amount / validOutputs, maxOutput);
        for (TileEntity te : mapPosSet.keySet())
        {
            if (!(te instanceof ICompressedFluidCapability)) return 0;
            ICompressedFluidCapability tube = (ICompressedFluidCapability) te;
            Direction face = mapPosSet.get(te).getOpposite();
            if (tube.canAccept(face, te.getPos()))
            {
                realMaxOutput = getLimitedValueForOutPut(realMaxOutput, maxOutput, te, simulate);
                if (realMaxOutput > 0)
                {
                    int fluid = tube.passCompressedFluid(realMaxOutput, y, simulate);
                    out += fluid;
                }
            }
        }
        return out;
    }

    public int getMaxOutput(Map<TileEntity, Direction> mapPosSet, int amount, int y)
    {
        int canAccept = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            Direction face = mapPosSet.get(te).getOpposite();
            if (te instanceof ICompressedFluidCapability
                    && ((ICompressedFluidCapability) te).canAccept(face, te.getPos()))
            {
                int realMaxOutput = amount;
                realMaxOutput = getLimitedValueForOutPut(realMaxOutput, maxOutput, te, true);
                if (realMaxOutput > 0)
                {
                    int fluid = ((ICompressedFluidCapability) te).passCompressedFluid(realMaxOutput, y, true);
                    if (fluid > 0) canAccept++;
                }
            }
        }
        return canAccept;
    }

    @Override
    public void checkForOutPuts()
    {
        if (world.isRemote) return;
        for (Direction face : Direction.values())
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            if (!(te instanceof TileEntityHighPressurePipe)
                    && te instanceof ICompressedFluidCapability
                    && ((ICompressedFluidCapability) te).canAccept(face.getOpposite(), currentPos))
            {
                addMachine(te, face);
            } else removeMachine(te);
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityHighPressurePipe;
    }
}
