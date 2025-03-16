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
    public final Map<BlockEntity, Integer> limitedOutPutMap = new ConcurrentHashMap<>();
    private final Map<BlockEntity, Direction> receiversContainer = new ConcurrentHashMap<>();
    private final Map<BlockEntity, Direction> receiversPressurizedContainer = new ConcurrentHashMap<>();
    public int outPut;
    public int oldOutPut = -1;
    public int outPutCount;
    int oldOutPutCount = -1;
    public TE master;
    public boolean isMaster;
    public boolean firstTick;
    public boolean inUse = false;
    protected boolean startBreaking;

    public BlockEntityMultiBlocksTube(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state)
    {
        super(tileEntityTypeIn, pos, state);
    }

    public void tick()
    {
        if (!firstTick && this.hasLevel())
        {
            firstTick = true;
            beforeInitialize();
            initializeMultiblockIfNecessary(true);
            onFirstLoad();
        }
        limitedOutPutMap.clear();
        if (this.hasLevel() && !isRemoved()) doTick();
    }
    
    public void beforeInitialize() {}
    
    public void doTick() {}

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
            final List<BlockEntityMultiBlocksTube<?>> connectedCables = new CopyOnWriteArrayList<>();
            final Stack<BlockEntityMultiBlocksTube<?>> traversingCables = new Stack<>();
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
            master.getReceiversPressurizedContainer().clear();
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

    public boolean canBeMaster(BlockEntity te)
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
        if (master != null && !master.isMaster()) sync();
        if (master == null)
        {
            if (!level.isClientSide() && !startBreaking)
            {
//                Utils.sendConsoleMessage("MultiBlock Pipe: " + this.getClass().toString() + " has no Master at " + pos);
//                Utils.sendConsoleMessage(" Break this pipe and try replace it, If this does not work, report the problem:");
//                Utils.sendConsoleMessage("https://github.com/Cassiobsk8/Industrial_Renewal/issues/new?template=bug_report.md");
                
            }
            return (TE) this;
        }
        return master;
    }

    public void setMaster(TE nMaster) {
        boolean wasMaster = isMaster;
        BlockEntityMultiBlocksTube oldMaster = this.master;
        this.master = nMaster;
        isMaster = nMaster == this;
        if (wasMaster != isMaster) {
            sync();
        }
        if (oldMaster != null) {
            oldMaster.master = nMaster;
            if (oldMaster != nMaster) oldMaster.isMaster = false;
        }
        if (!isMaster) {
            receiversContainer.clear();
        }
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
    
    public Map<BlockEntity, Direction> getReceiversPressurizedContainer()
    {
        return receiversPressurizedContainer;
    }
    
    public void cleanReceiversContainer() {
        Set<BlockEntity> toRemove = receiversContainer.keySet().stream().filter(BlockEntity::isRemoved).collect(Collectors.toSet());
        for (BlockEntity be : toRemove) {
            receiversContainer.remove(be);
        }
    }
    
    public void cleanReceiversPressurizedContainer() {
        Set<BlockEntity> toRemove = receiversPressurizedContainer.keySet().stream().filter(BlockEntity::isRemoved).collect(Collectors.toSet());
        for (BlockEntity be : toRemove) {
            receiversPressurizedContainer.remove(be);
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
        if (startBreaking || isRemoved() || machine == null) return;
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
    
    public void addReceiverPressurized(BlockEntity machine, Direction face)
    {
        if (machine == null) return;
        if (!isMaster())
        {
            getMaster().addReceiver(machine, face);
            return;
        }
        if (machine.getLevel().isClientSide() || receiversPressurizedContainer.containsKey(machine)) return;
        receiversPressurizedContainer.put(machine, face);
    }
    
    public void removeReceiverPressurized(BlockEntity machine)
    {
        if (startBreaking || isRemoved() || machine == null) return;
        if (!isMaster())
        {
            getMaster().removeReceiver(machine);
            return;
        }
        receiversPressurizedContainer.remove(machine);
    }
    
    public void startBreaking()
    {
        startBreaking = true;
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
        startBreaking = true;
//        Utils.debug("BREAK BLOCK");
        setRemoved();
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
