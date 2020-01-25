package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSolarPanel;
import cassiokf.industrialrenewal.blocks.BlockSolarPanelFrame;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntitySolarPanelFrame extends TileEntityMultiBlocksTube<TileEntitySolarPanelFrame>
{
    public final VoltsEnergyContainer energyContainer;
    public ItemStackHandler panelInv = new ItemStackHandler(1)
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

    private Set<BlockPos> panelReady = new HashSet<>();
    private int tick;
    private EnumFacing blockFacing;

    public TileEntitySolarPanelFrame()
    {
        this.energyContainer = new VoltsEnergyContainer(600, 0, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySolarPanelFrame.this.markDirty();
            }
        };
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        if (this.hasWorld() && !this.world.isRemote)
        {
            checkIfIsReady();
        }
    }

    @Override
    public void update()
    {
        if (this.hasWorld() && !this.world.isRemote)
        {
            if (isMaster())
            {
                int size = panelReady.size();
                energyContainer.setMaxEnergyStored(Math.max(600 * size, energyContainer.getEnergyStored()));
                if (size > 0) getEnergyFromSun();
                for (BlockPos posT : getPosSet().keySet())
                {
                    final TileEntity tileEntity = world.getTileEntity(posT);
                    if (tileEntity != null && !tileEntity.isInvalid())
                    {
                        EnumFacing facing = getPosSet().get(posT);
                        if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
                        {
                            final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                            if (consumer != null)
                            {
                                this.energyContainer.extractEnergy(consumer.receiveEnergy(energyContainer.extractEnergy(energyContainer.getMaxOutput(), true), false), false);
                            }
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
    public EnumFacing[] getFacesToCheck()
    {
        return EnumFacing.HORIZONTALS;
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
        if (hasPanel() && world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(EnumFacing.UP))
                && world.getSkylightSubtracted() == 0)
        {
            getMaster().getPanelReadySet().add(pos);
        } else getMaster().getPanelReadySet().remove(pos);
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            EnumFacing facing = getBlockFacing();
            boolean canConnect = face == facing || face == facing.rotateY() || face == facing.rotateYCCW();
            if (!canConnect) continue;
            BlockPos currentPos = pos.offset(face);
            IBlockState state = world.getBlockState(currentPos);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockSolarPanelFrame) && te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite());
            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).canReceive())
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
        }
    }

    public void getEnergyFromSun()
    {
        if (world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(EnumFacing.UP))
                && world.getSkylightSubtracted() == 0 && this.energyContainer.getEnergyStored() != this.energyContainer.getMaxEnergyStored())
        {
            int result = getMaster().energyContainer.getEnergyStored() + ((TileEntitySolarPanelBase.getGeneration(world, pos) * panelReady.size()) * getMultiplier());
            if (result > getMaster().energyContainer.getMaxEnergyStored())
            {
                result = getMaster().energyContainer.getMaxEnergyStored();
            }
            getMaster().energyContainer.setEnergyStored(result);
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
        return IRConfig.MainConfig.Main.panelFrameMultiplier;
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, panelInv);
    }

    public boolean hasPanel()
    {
        return !this.panelInv.getStackInSlot(0).isEmpty();
    }

    public ItemStack getPanel()
    {
        return panelInv.getStackInSlot(0);
    }

    public IItemHandler getPanelHandler()
    {
        return this.panelInv;
    }

    public EnumFacing getBlockFacing()
    {
        if (blockFacing == null)
        {
            IBlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlockSolarPanelFrame)
                blockFacing = state.getValue(BlockSolarPanelFrame.FACING);
            else blockFacing = EnumFacing.NORTH;
        }
        return blockFacing;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.panelInv.deserializeNBT(compound.getCompoundTag("bladeInv"));
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("bladeInv", this.panelInv.serializeNBT());
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        EnumFacing face = getBlockFacing();
        boolean canConnect = facing == face || facing == face.rotateY() || facing == face.rotateYCCW();
        return (capability == CapabilityEnergy.ENERGY && canConnect) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        EnumFacing face = getBlockFacing();
        boolean canConnect = facing == face || facing == face.rotateY() || facing == face.rotateYCCW();
        if (capability == CapabilityEnergy.ENERGY && canConnect)
            return CapabilityEnergy.ENERGY.cast(this.energyContainer);
        return super.getCapability(capability, facing);
    }
}
