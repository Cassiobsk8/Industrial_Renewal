package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityMultiBlockBase<TE extends TileEntityMultiBlockBase> extends TileEntitySync implements ITickable
{
    protected boolean firstTick = false;
    private boolean isMaster;
    private boolean breaking;
    private boolean startBreaking;
    private TE masterTE;
    private boolean masterChecked = false;
    private boolean faceChecked = false;
    private int faceIndex;
    protected final List<TE> machineTEList = new ArrayList<>();

    @Override
    public void update()
    {
        if (!firstTick)
        {
            firstTick = true;
            isMaster();
            if (isMaster())
            {
                startList();
                this.setMaster();
            }
            onFirstTick();
        }
        tick();
    }

    public void tick()
    {
    }

    public void onFirstTick()
    {
    }

    public TE getMaster()
    {
        if (isMaster) return (TE) this;
        if (masterTE == null || masterTE.isInvalid())
        {
            for (TE te : machineTEList)
            {
                if (te != null && te.isMaster())
                {
                    setMaster(te);
                    te.setMaster();
                    return masterTE;
                }
            }
            if (!world.isRemote && !startBreaking)
            {
                Utils.sendConsoleMessage("MultiBlock Machine: " + this.getBlockType().toString() + " has no Master at " + pos);
                Utils.sendConsoleMessage(" Break this machine and try replace it, If this does not work, report the problem:");
                Utils.sendConsoleMessage("https://github.com/Cassiobsk8/Industrial_Renewal/issues/new?template=bug_report.md");
            }
            return (TE) this;
        }
        return masterTE;
    }

    public void setMaster()
    {
        if (!isMaster()) return;
        for (TE te : machineTEList)
        {
            if (instanceOf(te))
            {
                te.setMaster(this);
            }
        }
    }

    public void setBlockList(List<BlockPos> list)
    {
        machineTEList.clear();
        for (BlockPos currentPos : list)
        {
            TileEntity te = world.getTileEntity(currentPos);
            if (instanceOf(te)) machineTEList.add((TE) te);
        }
        this.markDirty();
    }

    @Override
    public void sync()
    {
        if (isMaster) super.sync();
    }

    public void breakMultiBlocks()
    {
        startBreaking = true;
        if (!this.isMaster())
        {
            if (getMaster() != null && getMaster() != this)
            {
                getMaster().breakMultiBlocks();
            }
            return;
        }
        if (!breaking)
        {
            breaking = true;
            onMasterBreak();
            for (TE te : machineTEList)
            {
                if (te != null) world.setBlockToAir(te.getPos());
            }
        }
    }

    @Deprecated // For old save compatibility
    private void startList()
    {
        if (machineTEList.isEmpty())
        {
            setBlockList(getListOfBlockPositions(pos));
        }
    }

    @Deprecated // For old save compatibility
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(centerPosition);
    }

    public abstract boolean instanceOf(TileEntity tileEntity);

    public EnumFacing getMasterFacing()
    {
        if (faceChecked) return EnumFacing.byIndex(faceIndex);
        if (getMaster() == null)
        {
            return getBlockFace();
        }
        EnumFacing facing = getMaster().getBlockFace();
        faceChecked = true;
        faceIndex = facing.getIndex();
        return facing;
    }

    public EnumFacing getBlockFace()
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockMultiBlockBase)
            return state.getValue(BlockMultiBlockBase.FACING);
        return EnumFacing.NORTH;
    }

    public void onMasterBreak()
    {
    }

    public boolean isMaster()
    {
        if (masterChecked) return this.isMaster;

        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof BlockMultiBlockBase)) return false;
        isMaster = state.getValue(BlockMultiBlockBase.MASTER);
        masterChecked = true;
        return isMaster;
    }

    public void setMaster(TE master)
    {
        this.masterTE = master;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("master", this.isMaster());
        compound.setBoolean("checked", this.masterChecked);
        NBTTagList list = new NBTTagList();
        for (TE te : machineTEList)
        {
            if (te != null)
            {
                NBTTagCompound tag = NBTUtil.createPosTag(te.getPos());
                list.appendTag(tag);
            }
        }
        compound.setTag("list", list);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.isMaster = compound.getBoolean("master");
        this.masterChecked = compound.getBoolean("checked");
        NBTTagList list = compound.getTagList("list", Constants.NBT.TAG_LIST);
        machineTEList.clear();
        if (!list.isEmpty())
        {
            for (int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                TileEntity te = world.getTileEntity(NBTUtil.getPosFromTag(tag));
                if (instanceOf(te)) machineTEList.add((TE) te);
            }
        }
        super.readFromNBT(compound);
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(pos.add(-1D, -1D, -1D), pos.add(2D, 2D, 2D));
    }
}
