package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class TEMultiTankBase<T extends TEMultiTankBase> extends TileEntityMultiBlockBase<T>
{
    private boolean isBottom = false;
    private boolean isTop = false;
    public T bottomTE = (T) this;
    public T topTE = (T) this;

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(centerPosition);
    }

    @Override
    public void onFirstTick()
    {
        setParameters();
    }

    @Override
    public void onMasterBreak()
    {
        if (world.isRemote) return;
        TileEntity upTE = world.getTileEntity(pos.up(3));
        if (instanceOf(upTE))
        {
            ((T) upTE).setBottom(true);
            ((T) upTE).sync();
        }
        TileEntity downTE = world.getTileEntity(pos.down(3));
        if (instanceOf(downTE))
        {
            ((T) downTE).setTop(true);
            ((T) downTE).sync();
        }
    }

    public void setParameters()
    {
        if (!world.isRemote && isMaster())
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockMultiTankBase)
            {
                state = state.getActualState(world, getPos());
                setBottom(state.getValue(BlockMultiTankBase.DOWN) == 1);
                setTop(state.getValue(BlockMultiTankBase.TOP) == 1);
            }
        }
    }

    public void checkSize(T top)
    {
        TileEntity te = world.getTileEntity(pos.down(3));
        if (instanceOf(te))
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
            bottomTE = (T) this;
            topTE = top;
        }
        sync();
    }

    public abstract void setSize(int i);

    public TEMultiTankBase passValueDown(int i, T top)
    {
        TileEntity te = world.getTileEntity(pos.down(3));
        if (instanceOf(te))
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
        if (instanceOf(te))
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
