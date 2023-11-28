package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.block.abstracts.BlockTowerBase;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockEntityTowerBase<TE extends BlockEntityTowerBase<?>> extends BlockEntity3x3x3MachineBase<TE>{

    public ArrayList<TE> tower = null;

    public BlockEntityTowerBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityTowerBase;
    }

    public void setSelfBooleanProperty(){
        if(level == null) return;
        List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(getMaster().worldPosition);
        if(isAligned(true)){
            for(BlockPos blockPos : blocks){
                BlockEntity te = level.getBlockEntity(blockPos);
                BlockState blockState = level.getBlockState(blockPos);
                if(instanceOf(te)){
                    level.setBlockAndUpdate(blockPos, blockState.setValue(BlockTowerBase.TOP, false));
                    //Utils.debug("Updated BlockState", property, level.getBlockState(blockPos).getValue(property));
                }
            }
        }
        if(isAligned(false)){
            for(BlockPos blockPos : blocks){
                BlockEntity te = level.getBlockEntity(blockPos);
                BlockState blockState = level.getBlockState(blockPos);
                if(instanceOf(te)){
                    level.setBlockAndUpdate(blockPos, blockState.setValue(BlockTowerBase.BASE, false));
                    //Utils.debug("Updated BlockState", property, level.getBlockState(blockPos).getValue(property));
                }
            }
        }
    }

    public void setSelfBooleanProperty(BooleanProperty property, boolean bool){
        if(level == null) return;
        List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(getMaster().worldPosition);
        for(BlockPos blockPos : blocks){
            BlockEntity te = level.getBlockEntity(blockPos);
            BlockState blockState = level.getBlockState(blockPos);
            if(te instanceof BlockEntityTowerBase){
                level.setBlockAndUpdate(blockPos, blockState.setValue(property, bool));
                //Utils.debug("Updated BlockState", property, level.getBlockState(blockPos).getValue(property));
            }
        }
    }

    public boolean topAligned(){
        if(level == null) return false;
        TE master = getMaster();
        BlockPos relativePos = master.getBlockPos().above(3);
        BlockEntity relativeMaster = level.getBlockEntity(relativePos);

        return master.instanceOf(relativeMaster) && ((BlockEntityTowerBase<?>) relativeMaster).isMaster() &&
                ((BlockEntityTowerBase<?>) relativeMaster).getMasterFacing() == master.getMasterFacing();
    }

    public boolean botAligned(){
        if(level == null) return false;
        TE master = getMaster();
        BlockPos relativePos = master.getBlockPos().below(3);
        BlockEntity relativeMaster = level.getBlockEntity(relativePos);

        return master.instanceOf(relativeMaster) && ((BlockEntityTowerBase<?>) relativeMaster).isMaster() &&
                ((BlockEntityTowerBase<?>) relativeMaster).getMasterFacing() == master.getMasterFacing();
    }

    public boolean isAligned(boolean above){
        return above? topAligned() : botAligned();
    }

    public void setOtherBooleanProperty(BooleanProperty property, boolean bool, boolean above){
        if(level == null) return;
        if(isAligned(above)){
            TE master = getMaster();
            BlockPos relativePos = above? master.getBlockPos().above(3) : master.getBlockPos().below(3);
            BlockEntity relativeMaster = level.getBlockEntity(relativePos);
            ((BlockEntityTowerBase<?>) relativeMaster).setSelfBooleanProperty(property, bool);
        }
    }

    public TE getBase(){
        if(level == null) return null;
        TE currentTower = (TE)level.getBlockEntity(worldPosition);
        BlockPos currentPos = worldPosition;
        while(isAligned(false)){
            currentPos = currentPos.below(3);
            currentTower = (TE)level.getBlockEntity(currentPos);
            if(currentTower.isBase())
                return currentTower;
        }
        return currentTower;
    }

    public TE getTop(){
        if(tower != null && !tower.isEmpty()){
            return (TE)tower.get(tower.size()-1);
        }
        return null;
    }

    public TE getAbove(){
        if(level == null) return null;
        TE above = null;
        if(topAligned()){
            above = (TE) level.getBlockEntity(worldPosition.above(3));
        }
        return above;
    }

    public boolean isBase(){
        if(level == null) return false;
        if(getMaster() == null) return false;
        return level.getBlockState(getMaster().worldPosition).getValue(BlockTowerBase.BASE);
    }
    public boolean isTop(){
        if(level == null) return false;
        if(getMaster() == null) return false;
        return level.getBlockState(getMaster().worldPosition).getValue(BlockTowerBase.TOP);
    }

//    public void loadTower(){
//        TileEntityTowerBase<TE> chunk = this;
//        while(chunk != null){
//            tower.add(chunk);
//            chunk = chunk.getAbove();
//        }
//    }
    public void loadTower(){
        TE chunk = (TE) this;
        tower = new ArrayList<>();
        while(chunk != null){
            if(!tower.contains(chunk))
                tower.add(chunk);
            chunk = (TE) chunk.getAbove();
        }
    }

    public void addToTower(TE tile, ArrayList<TE> list){
        if(tower == null){
            tower = new ArrayList<>();
        }
        if(!tower.contains(tile))
            this.tower.add(tile);
        if(list != null){
            tower.addAll(list);
        }
    }

    public void removeTower(TE tile){
        if(tower != null && tower.contains(tile)){
            int index = tower.indexOf(tile);
            this.tower = new ArrayList<>(tower.subList(0, index));
        }
    }

    @Override
    public String toString() {
        return "TETB " + worldPosition;
    }
}
