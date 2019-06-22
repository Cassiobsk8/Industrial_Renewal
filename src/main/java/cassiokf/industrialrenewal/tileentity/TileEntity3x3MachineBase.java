package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.Fluid.TileFluidHandlerBase;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public abstract class TileEntity3x3MachineBase<TE extends TileEntity3x3MachineBase> extends TileFluidHandlerBase implements ICapabilityProvider
{
    private boolean master;
    private boolean breaking;

    @Override
    public void onLoad()
    {
        this.getIsMaster();
    }

    public TE getMaster()
    {
        List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            Block block = world.getBlockState(currentPos).getBlock();
            if (block instanceof Block3x3x3Base && ((TileEntity3x3MachineBase) world.getTileEntity(currentPos)).getIsMaster())
            {
                return (TE) world.getTileEntity(currentPos);
            }
        }
        return null;
    }

    public void breakMultiBlocks()
    {
        if (!this.getIsMaster())
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

    public boolean isMaster()
    {
        return this.master;
    }

    protected boolean getIsMaster()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof Block3x3x3Base)) return false;
        return state.getValue(Block3x3x3Base.MASTER);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("master", this.getIsMaster());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.master = compound.getBoolean("master");
        super.readFromNBT(compound);
    }
}
