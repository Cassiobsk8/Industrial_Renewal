package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class BlockEntityMultiBlocksTube<TE extends BlockEntityMultiBlocksTube<?>> extends BETubeBase
{
    final public Map<BlockEntity, Integer> limitedOutPutMap = new ConcurrentHashMap<>();
    final private Map<BlockEntity, Direction> receiversContainer = new ConcurrentHashMap<>();
    public int outPut;
    public int oldOutPut = -1;
    public int outPutCount;
    int oldOutPutCount = -1;
    private TE master;
    private boolean isMaster;
    public boolean firstTick;
    public boolean inUse = false;

    public BlockEntityMultiBlocksTube(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state)
    {
        super(tileEntityTypeIn, pos, state);
    }

    public void tick()
    {
        if (!firstTick && this.hasLevel())
        {
            firstTick = true;
            initializeMultiblockIfNecessary(true);
            onFirstLoad();
        }
        limitedOutPutMap.clear();
//        if (this.hasLevel() && !isRemoved()) doTick();
    }

    public void onFirstLoad()
    {
    }

    private void initializeMultiblockIfNecessary(){
        initializeMultiblockIfNecessary(false);
    }

    private void initializeMultiblockIfNecessary(boolean forceInit)
    {
        if(level == null) return;
        if (isTray()) return;
        if ((isMasterInvalid() && !this.isRemoved()) || forceInit)
        {
            //industrialrenewal.LOGGER.info("TRYING TO INIT MULTIBLOCK, Forced: "+forceInit);
            List<BlockEntityMultiBlocksTube<?>> connectedCables = new CopyOnWriteArrayList<>();
            Stack<BlockEntityMultiBlocksTube<?>> traversingCables = new Stack<>();
            TE master = (TE) this;
            traversingCables.add(this);
            while (!traversingCables.isEmpty())
            {
                BlockEntityMultiBlocksTube<?> storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = (TE) storage;
                }
                connectedCables.add(storage);
                for (Direction d : getFacesToCheck())
                {
                    BlockEntity te = level.getBlockEntity(storage.getBlockPos().relative(d));
                    if (instanceOf(te) && !connectedCables.contains(te))
                    {
                        traversingCables.add((TE) te);
                    }
                }
            }
            master.getReceivers().clear();
            master.getReceiversContainers().clear();
            if (canBeMaster(master))
            {
                for (BlockEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.setMaster((TE) master);
                    storage.checkForOutPuts(storage.getBlockPos());
                    storage.setChanged();
                }
            } else
            {
                for (BlockEntityMultiBlocksTube<?> storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.getReceivers().clear();
                    storage.setMaster(null);
                }
            }
            //Utils.debug("", connectedCables);
            setChanged();
        }
    }

    public void requestModelRefresh()
    {
        this.requestModelDataUpdate();
    }

    public boolean isTray()
    {
        return false;
    }

    private boolean canBeMaster(BlockEntity te)
    {
        return te != null;// && !(te instanceof TileEntityCableTray);
    }

    public boolean isMasterInvalid()
    {
        return master == null || master.isRemoved();
    }

    public Direction[] getFacesToCheck()
    {
        return Direction.values();
    }

    public abstract boolean instanceOf(BlockEntity te);

    public abstract void checkForOutPuts(BlockPos bPos);

    public boolean isMaster()
    {
        return isMaster;
    }

    public TE getMaster()
    {
        initializeMultiblockIfNecessary();
        return master;
    }

    public void setMaster(TE master)
    {
        this.master = master;
        isMaster = master == this;
        if (!isMaster) {
            receiversContainer.clear();
        }
//        requestModelRefresh();
    }
    
    public int getLimitedValueForOutPut(int value, int maxTransferAmount, BlockEntity storage, boolean simulate)
    {
        if (!limitedOutPutMap.containsKey(storage))
        {
            if (!simulate) limitedOutPutMap.put(storage, value);
            return Math.min(value, maxTransferAmount);
        }
        int currentValue = limitedOutPutMap.get(storage);
        int maxValue = maxTransferAmount - currentValue;
        maxValue = Math.min(value, maxValue);
        if (!simulate) limitedOutPutMap.put(storage, currentValue + maxValue);
        return maxValue;
    }
    
    public Map<BlockEntity, Direction> getReceiversContainers()
    {
        return receiversContainer;
    }
    
    public void cleanReceiversContainer() {
        Set<BlockEntity> toRemove = receiversContainer.keySet().stream().filter(BlockEntity::isRemoved).collect(Collectors.toSet());
        for (BlockEntity be : toRemove) {
            receiversContainer.remove(be);
        }
    }
    
    public void addReceiver(BlockEntity machine, Direction face)
    {
        if (machine == null) return;
        if (!isMaster())
        {
            getMaster().addReceiver(machine, face);
            return;
        }
        if (machine.getLevel().isClientSide() || receiversContainer.containsKey(machine)) return;
        receiversContainer.put(machine, face);
    }
    
    public void removeReceiver(BlockEntity machine)
    {
        if (isRemoved() || machine == null) return;
        if (!isMaster())
        {
            getMaster().removeReceiver(machine);
            return;
        }
        receiversContainer.remove(machine);
    }

    public Map<BlockEntity, Direction> getReceivers()
    {
        return receiversContainer;
    }

    @Override
    public void setRemoved()
    {
//        Utils.debug("SET REMOVED");
        super.setRemoved();
        if (master != null)
        {
            master.setMaster(null);
        }
    }

    public void breakBlock(){
        if(level == null) return;
//        Utils.debug("BREAK BLOCK");
        super.setRemoved();
        if (master != null) master.getMaster();
        else if (!isMaster) getMaster();
        for (Direction d : Direction.values())
        {
            BlockEntity te = level.getBlockEntity(getBlockPos().relative(d));
//            Utils.debug("", te instanceof BlockEntityMultiBlocksTube);
            if (te instanceof BlockEntityMultiBlocksTube)
            {
                ((BlockEntityMultiBlocksTube<?>) te).master = null;
                ((BlockEntityMultiBlocksTube<?>) te).initializeMultiblockIfNecessary();

            }
        }
    }
    @Override
    public void load(CompoundTag compound)
    {
        isMaster = compound.getBoolean("isMaster");
        outPut = compound.getInt("out");
        outPutCount = compound.getInt("count");
        super.load(compound);
    }


    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putBoolean("isMaster", isMaster);
        compound.putInt("out", outPut);
        compound.putInt("count", outPutCount);
        super.saveAdditional(compound);
    }
}
