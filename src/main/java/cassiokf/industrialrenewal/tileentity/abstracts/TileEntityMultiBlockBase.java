package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class TileEntityMultiBlockBase<TE extends TileEntityMultiBlockBase> extends TileEntitySync implements ITickableTileEntity
{
    private boolean isMaster;
    private boolean breaking;
    private boolean startBreaking;
    private TE masterTE;
    private boolean masterChecked = false;
    private boolean faceChecked = false;
    private int faceIndex;
    protected boolean firstTick = false;

    public TileEntityMultiBlockBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public final void tick()
    {
        if (!firstTick)
        {
            firstTick = true;
            isMaster();
            if (isMaster()) this.setMaster();
            onFirstTick();
        }
        onTick();
    }

    public void onTick()
    {
    }

    public void onFirstTick()
    {
    }

    public TE getMaster()
    {
        if (isMaster) return (TE) this;
        if (masterTE == null || masterTE.isRemoved())
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
                Utils.sendConsoleMessage("MultiBlock Machine: " + this.getBlockState().getBlock().getRegistryName().toString() + " has no Master at " + pos);
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
            List<BlockPos> list = getListOfBlockPositions(pos);
            for (BlockPos currentPos : list)
            {
                Block block = world.getBlockState(currentPos).getBlock();
                if (block instanceof BlockMultiBlockBase) world.removeBlock(currentPos, false);
            }
        }
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(centerPosition);
    }

    public abstract boolean instanceOf(TileEntity tileEntity);

    public Direction getMasterFacing()
    {
        if (faceChecked) return Direction.byIndex(faceIndex);
        Direction facing = forceBlockFaceCheck();
        faceChecked = true;
        faceIndex = facing.getIndex();
        return facing;
    }

    public Direction forceBlockFaceCheck()
    {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockMultiBlockBase)
            return state.get(BlockMultiBlockBase.FACING);
        return Direction.NORTH;
    }

    public void onMasterBreak()
    {
    }

    public boolean isMaster()
    {
        if (masterChecked) return this.isMaster;

        BlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof BlockMultiBlockBase)) return false;
        isMaster = state.get(BlockMultiBlockBase.MASTER);
        masterChecked = true;
        return isMaster;
    }

    public void setMaster(TE master)
    {
        this.masterTE = master;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("master", this.isMaster());
        compound.putBoolean("checked", this.masterChecked);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        this.isMaster = compound.getBoolean("master");
        this.masterChecked = compound.getBoolean("checked");
        super.read(compound);
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(pos.add(-1D, -1D, -1D), pos.add(2D, 2D, 2D));
    }
}
