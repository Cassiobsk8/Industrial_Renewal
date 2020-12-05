package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class TileEntityMultiBlocksTube<TE extends TileEntityMultiBlocksTube> extends TileEntitySync implements ITickableTileEntity
{
    private TE master;
    private boolean isMaster;
    final public Map<TileEntity, Integer> limitedOutPutMap = new ConcurrentHashMap<>();
    final private Map<TileEntity, Direction> machineContainer = new ConcurrentHashMap<>();
    public int outPut;
    int outPutCount;
    boolean firstTick = false;
    protected boolean inUse = false;
    private boolean startBreaking;

    public TileEntityMultiBlocksTube(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public final void tick()
    {
        if (!firstTick)
        {
            firstTick = true;
            beforeInitialize();
            initializeMultiblockIfNecessary();
            onFirstTick();
        }
        onTick();
        limitedOutPutMap.clear();
    }

    public void onTick()
    {
    }

    public void beforeInitialize()
    {

    }

    public void onFirstTick()
    {
    }

    public void initializeMultiblockIfNecessary()
    {
        initializeMultiblockIfNecessary(false);
    }

    public void initializeMultiblockIfNecessary(boolean forced)
    {
        if (isTray()) return;
        if ((forced || isMasterInvalid()))
        {
            if (IRConfig.Main.debugMessages.get())
                System.out.println("initialize " + forced + " " + this + " " + pos);
            List<TileEntityMultiBlocksTube> connectedCables = new CopyOnWriteArrayList<>();
            Stack<TileEntityMultiBlocksTube> traversingCables = new Stack<>();
            TE master = (TE) this;
            traversingCables.add(this);
            while (!traversingCables.isEmpty())
            {
                TileEntityMultiBlocksTube storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = (TE) storage;
                }
                connectedCables.add(storage);
                for (Direction d : getFacesToCheck())
                {
                    TileEntity te = world.getTileEntity(storage.getPos().offset(d));
                    if (instanceOf(te) && !connectedCables.contains(te))
                    {
                        traversingCables.add((TE) te);
                    }
                }
            }
            master.getMachineContainers().clear();
            if (canBeMaster(master))
            {
                for (TileEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.setMaster(master);
                    storage.checkForOutPuts();
                    storage.markDirty();
                }
            } else
            {
                for (TileEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    else if (!canBeMaster(master) && canBeMaster(storage))
                    {
                        master = (TE) storage;
                        break;
                    }
                }
                if (!canBeMaster(master)) return;
                for (TileEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.setMaster(master);
                    storage.checkForOutPuts();
                    storage.markDirty();
                }
            }
            sync();
        }
    }

    public int getLimitedValueForOutPut(int value, int maxTransferAmount, TileEntity storage, boolean simulate)
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

    public boolean isTray()
    {
        return false;
    }

    private boolean canBeMaster(TileEntity te)
    {
        return te != null && !(te instanceof TileEntityCableTray);
    }

    public boolean isMasterInvalid()
    {
        return master == null || master.isRemoved();
    }

    public Direction[] getFacesToCheck()
    {
        return Direction.values();
    }

    public abstract boolean instanceOf(TileEntity te);

    public abstract void checkForOutPuts();

    public boolean isMaster()
    {
        return isMaster;
    }

    public TE getMaster()
    {
        if (isMaster) return (TE) this;
        initializeMultiblockIfNecessary();
        if (master != null && !master.isMaster()) sync();
        if (master == null)
        {
            if (!world.isRemote && !startBreaking)
            {
                Utils.sendConsoleMessage("MultiBlock Pipe: " + this.getClass().toString() + " has no Master at " + pos);
                Utils.sendConsoleMessage(" Break this pipe and try replace it, If this does not work, report the problem:");
                Utils.sendConsoleMessage("https://github.com/Cassiobsk8/Industrial_Renewal/issues/new?template=bug_report.md");

            }
            return (TE) this;
        }
        return master;
    }

    public void setMaster(TE master)
    {
        boolean wasMaster = isMaster;
        this.master = master;
        isMaster = master == this;
        if (wasMaster != isMaster)
        {
            final BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 2);
        }
        if (!isMaster) machineContainer.clear();
    }

    public Map<TileEntity, Direction> getMachineContainers()
    {
        return machineContainer;
    }

    public void addMachine(TileEntity machine, Direction face)
    {
        if (machine == null) return;
        if (!isMaster())
        {
            getMaster().addMachine(machine, face);
            return;
        }
        machineContainer.put(machine, face);
    }

    public void removeMachine(TileEntity machine)
    {
        if (startBreaking || isRemoved() || machine == null) return;
        if (!isMaster())
        {
            getMaster().removeMachine(machine);
            return;
        }
        machineContainer.remove(machine);
    }

    public void startBreaking()
    {
        startBreaking = true;
    }

    @Override
    public void onBlockBreak()
    {
        startBreaking = true;
        super.onBlockBreak();
        this.remove();
        if (IRConfig.Main.debugMessages.get()) System.out.println("Breaking " + this.getBlockState().getBlock().getNameTextComponent().getFormattedText());
        for (Direction d : Direction.values())
        {
            TileEntity te = world.getTileEntity(pos.offset(d));
            if (instanceOf(te))
            {
                if (te instanceof TileEntityCableTray)
                    ((TileEntityCableTray) te).refreshConnections();
                else
                    ((TileEntityMultiBlocksTube) te).initializeMultiblockIfNecessary(true);
            }
        }
    }

    @Override
    public void read(CompoundNBT compound)
    {
        isMaster = compound.getBoolean("isMaster");
        if (hasWorld() && world.isRemote)
        {
            TE te = (TE) world.getTileEntity(BlockPos.fromLong(compound.getLong("masterPos")));
            if (te != null) master = te;
        }
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("isMaster", isMaster);
        if (master != null && !master.isRemoved()) compound.putLong("masterPos", master.getPos().toLong());
        return super.write(compound);
    }
}
