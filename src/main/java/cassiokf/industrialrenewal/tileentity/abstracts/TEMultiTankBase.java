package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class TEMultiTankBase<T extends TEMultiTankBase> extends TileEntityMultiBlockBase<T>
{
    protected final List<T> machines = new ArrayList<>();
    public T bottomTE = (T) this;
    public T topTE = (T) this;
    private boolean isBottom = false;
    private boolean isTop = false;

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(centerPosition);
    }

    @Override
    public void onFirstTick()
    {
        if (isMaster())
        {
            reachTop();
        }
    }

    @Override
    public void onMasterBreak()
    {
        if (world.isRemote) return;
        this.invalidate();
        TileEntity upTE = world.getTileEntity(pos.up(3));
        if (instanceOf(upTE) && !upTE.isInvalid())
        {
            ((T) upTE).setBottom(true);
            ((T) upTE).reachTop();
        }
        TileEntity downTE = world.getTileEntity(pos.down(3));
        if (instanceOf(downTE) && !downTE.isInvalid())
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
        if (instanceOf(te) && !te.isInvalid())
        {
            bottomTE = (T) ((T) te).passValueDown(1, top);
        }
        else
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
        if (instanceOf(te) && !te.isInvalid())
        {
            isBottom = false;
            sync();
            return ((T) te).passValueDown(i + 1, top);
        }
        else
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
        if (instanceOf(te) && !te.isInvalid())
        {
            isTop = false;
            ((TEMultiTankBase<?>) te).reachTop();
        }
        else
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

    public void setBottom(boolean value)
    {
        isBottom = value;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("bottom", isBottom);
        compound.setBoolean("isTop", isTop);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        isBottom = compound.getBoolean("bottom");
        isTop = compound.getBoolean("isTop");
        super.readFromNBT(compound);
    }
}
