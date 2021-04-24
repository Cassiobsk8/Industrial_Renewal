package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class TEMultiTankBase<T extends TEMultiTankBase> extends TileEntityMultiBlockBase<T>
{
    protected final List<T> machines = new ArrayList<>();
    private boolean isBottom = false;
    private boolean isTop = false;
    public T bottomTE = (T) this;
    public T topTE = (T) this;

    public TEMultiTankBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(centerPosition);
    }

    @Override
    public void onFirstTick()
    {
        if (isMaster()) {
            reachTop();
        }
    }

    @Override
    public void onMasterBreak()
    {
        if (world.isRemote) return;
        this.remove();
        TileEntity upTE = world.getTileEntity(pos.up(3));
        if (instanceOf(upTE) && !upTE.isRemoved() && ((TEMultiTankBase)upTE).isMaster())
        {
            ((T) upTE).setBottom(true);
            ((T) upTE).sync();
        }
        TileEntity downTE = world.getTileEntity(pos.down(3));
        if (instanceOf(downTE) && !downTE.isRemoved() && ((TEMultiTankBase)downTE).isMaster())
        {
            ((T) downTE).setTop(true);
            ((T) downTE).sync();
        }
    }

    public void checkSize(T top)
    {
        TileEntity te = world.getTileEntity(pos.down(3));
        top.machines.clear();
        top.machines.add(this);
        if (instanceOf(te) && !te.isRemoved() && ((TEMultiTankBase)te).isMaster())
        {
            bottomTE = (T) ((T) te).passValueDown(1, top);
        } else
        {
            isBottom = true;
            bottomTE = (T) this;
            topTE = top;
        }

        if (isBottom)
        {
            setSize(1);
            topTE = top;
        }
        sync();
    }

    public abstract void setSize(int i);

    public TEMultiTankBase passValueDown(int i, T top)
    {
        top.machines.add(this);
        TileEntity te = world.getTileEntity(pos.down(3));
        if (instanceOf(te) && !te.isRemoved() && ((TEMultiTankBase)te).isMaster())
        {
            isBottom = false;
            sync();
            return ((T) te).passValueDown(i + 1, top);
        } else
        {
            isBottom = true;
            topTE = top;
            setSize(i + 1);
            return this;
        }
    }

    public void reachTop()
    {
        TileEntity te = world.getTileEntity(pos.up(3));
        if (instanceOf(te) && !te.isRemoved() && ((TEMultiTankBase)te).isMaster())
        {
            isTop = false;
            ((TEMultiTankBase<?>) te).reachTop();
        } else
        {
            isTop = true;
            checkSize((T) this);
        }
    }

    public T getBottomTE()
    {
        return bottomTE;
    }

    public T getTopTE()
    {
        return topTE;
    }

    public void setBottom(boolean value)
    {
        isBottom = value;
    }

    public void setTop(boolean value)
    {
        isTop = value;
        if (isTop) checkSize((T) this);
        else reachTop();
    }

    public boolean isBottom()
    {
        return isBottom;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("bottom", isBottom);
        compound.putBoolean("isTop", isTop);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        isBottom = compound.getBoolean("bottom");
        isTop = compound.getBoolean("isTop");
        super.read(compound);
    }
}
