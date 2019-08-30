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

public abstract class TileEntity3x3MachineBase<TE extends TileEntity3x3MachineBase> extends TileFluidHandlerBase implements ICapabilityProvider
{
    private boolean master;
    private boolean breaking;
    private TE masterTE;
    private boolean masterChecked = false;
    private boolean faceChecked = false;
    private int faceIndex;

    @Override
    public void onLoad()
    {
        this.getMaster();
    }

    public TE getMaster()
    {
        if (masterTE == null || masterTE.isInvalid())
        {
            List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(this.pos);
            for (BlockPos currentPos : list)
            {
                TileEntity te = world.getTileEntity(currentPos);
                if (te != null && te instanceof TileEntity3x3MachineBase && ((TileEntity3x3MachineBase) te).isMaster()
                        && instanceOf(te))
                {
                    masterTE = (TE) te;
                    return masterTE;
                }
            }
            return null;
        }
        return masterTE;
    }

    public abstract boolean instanceOf(TileEntity tileEntity);

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
            List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(this.pos);
            for (BlockPos currentPos : list)
            {
                Block block = world.getBlockState(currentPos).getBlock();
                if (block instanceof Block3x3x3Base) world.setBlockToAir(currentPos);
            }
        }
    }

    public EnumFacing getMasterFacing()
    {
        if (faceChecked) return EnumFacing.byIndex(faceIndex);

        EnumFacing facing = world.getBlockState(getMaster().getPos()).getValue(Block3x3x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.getIndex();
        return facing;
    }

    public boolean isMaster()
    {
        if (masterChecked) return this.master;

        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof Block3x3x3Base)) return false;
        master = state.getValue(Block3x3x3Base.MASTER);
        masterChecked = true;
        return master;
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
        this.master = compound.getBoolean("master");
        this.masterChecked = compound.getBoolean("checked");
        super.readFromNBT(compound);
    }
}
