package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSolarPanel;
import cassiokf.industrialrenewal.blocks.BlockSolarPanelFrame;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static cassiokf.industrialrenewal.init.TileRegistration.FPANEL_TILE;

public class TileEntitySolarPanelFrame extends TileEntityMultiBlocksTube<TileEntitySolarPanelFrame>
{
    public LazyOptional<IItemHandler> panelInv = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private Set<BlockPos> panelReady = new HashSet<>();
    private int tick;
    private Direction blockFacing;

    public TileEntitySolarPanelFrame()
    {
        super(FPANEL_TILE.get());
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return Block.getBlockFromItem(stack.getItem()) instanceof BlockSolarPanel;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntitySolarPanelFrame.this.Sync();
            }
        };
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(600, 0, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySolarPanelFrame.this.markDirty();
            }
        };
    }

    @Override
    public void onFirstLoad()
    {
        super.onLoad();
        if (this.hasWorld() && !world.isRemote)
        {
            checkIfIsReady();
        }
    }

    @Override
    public void doTick()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            if (isMaster())
            {
                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                int size = panelReady.size();
                energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(600 * size, thisEnergy.getEnergyStored())));
                if (size > 0) getEnergyFromSun();
                for (BlockPos posT : getPosSet().keySet())
                {
                    final TileEntity tileEntity = world.getTileEntity(posT);
                    if (tileEntity != null && !tileEntity.isRemoved())
                    {
                        Direction facing = getPosSet().get(posT);
                        final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
                        if (consumer != null)
                        {
                            thisEnergy.extractEnergy(consumer.receiveEnergy(thisEnergy.extractEnergy(1024, true), false), false);
                        }
                    }
                }
            }

            if (tick >= 20)
            {
                tick = 0;
                checkIfIsReady();
            }
            tick++;
        }
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        return new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntitySolarPanelFrame;
    }

    public Set<BlockPos> getPanelReadySet()
    {
        return panelReady;
    }

    public void checkIfIsReady()
    {
        if (hasPanel() && world.getLightManager().hasLightWork() && world.canBlockSeeSky(pos.offset(Direction.UP))
                && world.getSkylightSubtracted() == 0)
        {
            getMaster().getPanelReadySet().add(pos);
        } else getMaster().getPanelReadySet().remove(pos);
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (Direction face : Direction.values())
        {
            Direction facing = getBlockFacing();
            boolean canConnect = face == facing || face == facing.rotateY() || face == facing.rotateYCCW();
            if (!canConnect) continue;
            BlockPos currentPos = pos.offset(face);
            BlockState state = world.getBlockState(currentPos);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockSolarPanelFrame) && te != null
                    && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();
            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
        }
    }

    public void getEnergyFromSun()
    {
        IEnergyStorage thisEnergy = getMaster().energyStorage.orElse(null);
        if (world.getLightManager().hasLightWork() && world.canBlockSeeSky(pos.offset(Direction.UP))
                && world.getSkylightSubtracted() == 0 && (thisEnergy.getEnergyStored() != thisEnergy.getMaxEnergyStored()))
        {
            int result = thisEnergy.getEnergyStored() + ((TileEntitySolarPanelBase.getGeneration(world, pos) * panelReady.size()) * getMultiplier());
            if (result > thisEnergy.getMaxEnergyStored())
            {
                result = thisEnergy.getMaxEnergyStored();
            }
            final int energy = result;
            energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(energy));
        }
    }

    @Override
    public void removeMachine(BlockPos ownPos, BlockPos machinePos)
    {
        getPanelReadySet().remove(ownPos);
        super.removeMachine(ownPos, machinePos);
    }

    private int getMultiplier()
    {
        return IRConfig.Main.panelFrameMultiplier.get();
    }

    public boolean hasPanel()
    {
        return !panelInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public ItemStack getPanel()
    {
        return panelInv.orElse(null).getStackInSlot(0);
    }

    public IItemHandler getPanelHandler()
    {
        return panelInv.orElse(null);
    }

    public Direction getBlockFacing()
    {
        if (blockFacing == null)
        {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockSolarPanelFrame)
                blockFacing = state.get(BlockSolarPanelFrame.FACING);
            else blockFacing = Direction.NORTH;
        }
        return blockFacing;
    }

    @Override
    public void remove()
    {
        Utils.dropInventoryItems(world, pos, panelInv.orElse(null));
        super.remove();
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        panelInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        panelInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        return super.write(compound);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        Direction face = getBlockFacing();
        boolean canConnect = facing == face || facing == face.rotateY() || facing == face.rotateYCCW();
        if (capability == CapabilityEnergy.ENERGY && canConnect)
            return getMaster().energyStorage.cast();
        return super.getCapability(capability, facing);
    }
}
