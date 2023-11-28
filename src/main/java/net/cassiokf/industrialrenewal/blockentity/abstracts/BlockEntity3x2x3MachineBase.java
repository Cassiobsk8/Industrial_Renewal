package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.block.abstracts.Block3x2x3Base;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BlockEntity3x2x3MachineBase<TE extends BlockEntity3x2x3MachineBase<?>> extends BlockEntity3x3x3MachineBase<TE>{
    public BlockEntity3x2x3MachineBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }
    public BlockPos masterPos = worldPosition;

    @Override
    public TE getMaster() {
        if(level == null) return null;
        BlockEntity te = level.getBlockEntity(masterPos);
        if(te != null && te instanceof BlockEntity3x2x3MachineBase
                && ((BlockEntity3x2x3MachineBase<?>) te).isMaster()
                && instanceOf(te)) {
            masterTE = (TE) te;
            return masterTE;
        }
        else if (te == null){
            List<BlockPos> list = Utils.getBlocksIn3x2x3Centered(worldPosition);
            for (BlockPos currentPos : list)
            {
                BlockEntity te2 = level.getBlockEntity(currentPos);
                if (te2 != null && te2 instanceof BlockEntity3x3x3MachineBase
                        && ((BlockEntity3x2x3MachineBase<?>) te2).isMaster()
                        && instanceOf(te2))
                {
                    masterTE = (TE) te2;
                    return masterTE;
                }
            }
        }

        return null;
    }

//    @Override
//    public void setRemoved() {
//        BlockEntity3x2x3MachineBase te = (BlockEntity3x2x3MachineBase) level.getBlockEntity(masterPos);
//        if(te != null)
//            te.breakMultiBlocks();
//        super.setRemoved();
//    }

    @Override
    public Direction getMasterFacing()
    {
        if (faceChecked) return Direction.from3DDataValue(faceIndex);
        if(level == null || getMaster() == null || level.getBlockState(getMaster().worldPosition) == null)
            return Direction.NORTH;

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x2x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public Direction getMasterFacingDirect(){
        if(level == null) return Direction.NORTH;
        if (faceChecked) return Direction.from3DDataValue(faceIndex);
        Direction facing = level.getBlockState(worldPosition).getValue(Block3x2x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x2x3Centered(centerPosition);
    }

    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntity3x2x3MachineBase;
    }

    @Override
    public void breakMultiBlocks(BlockState state) {
        if(level == null) return;
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
                if (block instanceof Block3x2x3Base) level.removeBlock(currentPos, false);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putLong("masterPos", masterPos.asLong());
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        masterPos = BlockPos.of(compoundTag.getLong("masterPos"));
        super.load(compoundTag);
    }
}
