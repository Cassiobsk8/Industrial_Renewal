package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.block.abstracts.Block3x3x3Base;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public abstract class BlockEntity3x3x3MachineBase<TE extends BlockEntity3x3x3MachineBase<?>> extends BlockEntitySyncable implements ICapabilityProvider {

    protected boolean master;
    protected boolean breaking;
    protected TE masterTE;
    protected boolean masterChecked = false;
    protected boolean faceChecked = false;
    protected int faceIndex;

    public BlockEntity3x3x3MachineBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public void onLoad() {

    }

    public TE getMaster() {
        if(level == null) return null;
        if (masterTE == null || masterTE.isRemoved())
        {
            List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(worldPosition);
            for (BlockPos currentPos : list)
            {
                BlockEntity te = level.getBlockEntity(currentPos);
                if (te instanceof BlockEntity3x3x3MachineBase
                        && ((BlockEntity3x3x3MachineBase<?>) te).isMaster()
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

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x3x3Centered(centerPosition);
    }

    public abstract boolean instanceOf(BlockEntity tileEntity);

    public void breakMultiBlocks(BlockState state)
    {
        if(level == null) return;
        //Utils.debug("breaking block", isMaster());
        if (!this.isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().breakMultiBlocks(state);
            }
            return;
        }
        if (!breaking)
        {
            breaking = true;
            onMasterBreak();
            List<BlockPos> list = getListOfBlockPositions(worldPosition);
            for (BlockPos currentPos : list)
            {
                Block block = level.getBlockState(currentPos).getBlock();
                if (block instanceof Block3x3x3Base) level.removeBlock(currentPos, false);
            }
        }
    }

    public void onMasterBreak()
    {
    }

    public Direction getMasterFacing()
    {
        if(level == null) return Direction.NORTH;
        if (faceChecked) return Direction.from3DDataValue(faceIndex);

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x3x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public boolean isMaster()
    {
        if (masterChecked) return this.master;

        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof Block3x3x3Base)) return false;
        master = state.getValue(Block3x3x3Base.MASTER);
        masterChecked = true;
        return master;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putBoolean("master", this.isMaster());
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        this.master = compoundTag.getBoolean("master");
        super.load(compoundTag);
    }
}
