package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class TileEntityMultiBlockBase<TE extends TileEntityMultiBlockBase> extends TileEntitySyncable implements ITickable
{
    private boolean isMaster;
    private boolean breaking;
    private boolean startBreaking;
    private TE masterTE;
    private boolean masterChecked = false;
    private boolean faceChecked = false;
    private int faceIndex;
    boolean firstTick = false;

    @Override
    public void update()
    {
        if (!firstTick)
        {
            firstTick = true;
            isMaster();
            if (isMaster()) this.setMaster();
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
        if (masterTE == null || masterTE.isInvalid())
        {
            List<BlockPos> list = MachinesUtils.getBlocksIn3x3x3Centered(this.pos);
            for (BlockPos currentPos : list)
            {
                TileEntity te = world.getTileEntity(currentPos);
                if (te instanceof TileEntityMultiBlockBase
                        && ((TileEntityMultiBlockBase) te).isMaster()
                        && instanceOf(te))
                {
                    setMaster((TE) te);
                    ((TE) te).setMaster();
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
        List<BlockPos> list = getListOfBlockPositions(pos);
        for (BlockPos currentPos : list)
        {
            TileEntity te = world.getTileEntity(currentPos);
            if (te instanceof TileEntityMultiBlockBase && instanceOf(te))
            {
                ((TileEntityMultiBlockBase) te).setMaster(this);
            }
        }
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
            List<BlockPos> list = getListOfBlockPositions(pos);
            for (BlockPos currentPos : list)
            {
                Block block = world.getBlockState(currentPos).getBlock();
                if (block instanceof BlockMultiBlockBase) world.setBlockToAir(currentPos);
            }
        }
    }

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
            IBlockState state = world.getBlockState(pos);
            if (state.getProperties().containsKey(BlockHorizontalFacing.FACING))
                return state.getValue(BlockHorizontalFacing.FACING);
            return EnumFacing.NORTH;
        }
        EnumFacing facing = world.getBlockState(getMaster().getPos()).getValue(BlockMultiBlockBase.FACING);
        faceChecked = true;
        faceIndex = facing.getIndex();
        return facing;
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
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.isMaster = compound.getBoolean("master");
        this.masterChecked = compound.getBoolean("checked");
        super.readFromNBT(compound);
    }
}
