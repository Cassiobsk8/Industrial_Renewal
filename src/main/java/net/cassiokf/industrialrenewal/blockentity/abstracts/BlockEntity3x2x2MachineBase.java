package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.block.abstracts.Block3x2x2Base;
import net.cassiokf.industrialrenewal.block.abstracts.Block3x3x2Base;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BlockEntity3x2x2MachineBase<TE extends BlockEntity3x2x2MachineBase<?>> extends BlockEntity3x3x3MachineBase<TE>{
    public BlockEntity3x2x2MachineBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }
    public BlockPos masterPos = worldPosition;

    @Override
    public TE getMaster() {
        if(level == null) return null;
        //return super.getMaster();
        BlockEntity te = level.getBlockEntity(masterPos);
        //Utils.debug("master pos", worldPosition, masterPos, te);
        if(te instanceof BlockEntity3x2x2MachineBase
                && ((BlockEntity3x2x2MachineBase<?>) te).isMaster()
                && instanceOf(te))
        {
            masterTE = (TE) te;
            return masterTE;
        }
        return null;
    }

//    @Override
//    public void setRemoved() {
//        BlockEntity3x2x2MachineBase te = (BlockEntity3x2x2MachineBase) level.getBlockEntity(masterPos);
//        if(te != null)
//            te.breakMultiBlocks();
//        super.setRemoved();
//    }

    @Override
    public Direction getMasterFacing()
    {
        if(level == null) return Direction.NORTH;
        if (faceChecked) return Direction.from3DDataValue(faceIndex);

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x2x2Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition, Direction facing)
    {
        return Utils.getBlocksIn3x2x2Centered(centerPosition, facing);
    }

    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntity3x2x2MachineBase;
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
            List<BlockPos> list = getListOfBlockPositions(worldPosition, state.getValue(Block3x3x2Base.FACING));
            for (BlockPos currentPos : list)
            {
                Block block = getBlockState().getBlock();
                if (block instanceof Block3x2x2Base) level.removeBlock(currentPos, false);
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
