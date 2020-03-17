package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.Block3x3x3Base;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public abstract class TileEntity3x3MachineBase<TE extends TileEntity3x3MachineBase> extends TileEntitySyncable implements ICapabilityProvider
{
    private boolean isMaster;
    private boolean breaking;
    private TE masterTE;
    private boolean masterChecked = false;
    private boolean faceChecked = false;
    private int faceIndex;

    @Override
    public void onLoad()
    {
        isMaster();
        if (isMaster()) this.setMaster();
    }

    public TE getMaster()
    {
        if (masterTE == null || masterTE.isInvalid())
        {
            List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(this.pos);
            for (BlockPos currentPos : list)
            {
                TileEntity te = world.getTileEntity(currentPos);
                if (te instanceof TileEntity3x3MachineBase
                        && ((TileEntity3x3MachineBase) te).isMaster()
                        && instanceOf(te))
                {
                    masterTE = (TE) te;
                    ((TE) te).setMaster();
                    return masterTE;
                }
            }
            return null;
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
            if (te instanceof TileEntity3x3MachineBase && instanceOf(te))
            {
                ((TileEntity3x3MachineBase) te).setMaster(this);
            }
        }
    }

    public void breakMultiBlocks()
    {
        if (!this.isMaster())
        {
            if (getMaster() != null)
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
                if (block instanceof Block3x3x3Base) world.setBlockToAir(currentPos);
            }
        }
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x3x3Centered(centerPosition);
    }

    public abstract boolean instanceOf(TileEntity tileEntity);

    public EnumFacing getMasterFacing()
    {
        if (faceChecked) return EnumFacing.byIndex(faceIndex);
        if (getMaster() == null) return EnumFacing.NORTH;
        EnumFacing facing = world.getBlockState(getMaster().getPos()).getValue(Block3x3x3Base.FACING);
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
        if (!(state.getBlock() instanceof Block3x3x3Base)) return false;
        isMaster = state.getValue(Block3x3x3Base.MASTER);
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
