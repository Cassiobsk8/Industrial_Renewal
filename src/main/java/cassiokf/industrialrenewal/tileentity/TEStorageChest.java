package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public class TEStorageChest extends TEMultiTankBase<TEStorageChest>
{
    private static final int slots = 99;
    public final CustomItemStackHandler inventory = new CustomItemStackHandler(slots)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TEStorageChest.this.markDirty();
        }

        @Override
        public void itemsToSpawn(List<ItemStack> list)
        {
            for (ItemStack item : list) TEStorageChest.this.spawnItems(item);
        }
    };
    public int additionalLines;
    public int currentLine;
    public String search = "";
    public boolean searchActive = IRConfig.Main.searchBarStartFocused.get();

    public TEStorageChest(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x3x2Centered(centerPosition, getMasterFacing());
    }

    public void guiButtonClick(int id, PlayerEntity player)
    {
        if (id == 1 && currentLine > 0)
        {
            currentLine--;
        }
        else if (id == 2 && currentLine < additionalLines)
        {
            currentLine++;
        }
        openGui(player, false);
    }

    public void openGui(PlayerEntity player, boolean resetLine)
    {
        if (this.isRemoved()) return;
        if (resetLine)
        {
            currentLine = 0;
            search = "";
            searchActive = IRConfig.Main.searchBarStartFocused.get();
        }
        if (!world.isRemote)
        {
            player.openGui(IndustrialRenewal.instance, GUIHandler.STORAGECHEST, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public void setLineValues(int currentLine, int additionalLines)
    {
        this.currentLine = currentLine;
        this.additionalLines = additionalLines;
    }

    @Override
    public void setSize(int i)
    {
        int newCapacity = slots * i;
        if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;

        additionalLines = (newCapacity / 11) - 6;
        currentLine = 0;
        inventory.setInvSize(newCapacity);
        NetworkHandler.INSTANCE.sendToAll(new PacketStorageChest(this));
        markDirty();
    }

    @Override
    public void onMasterBreak()
    {
        super.onMasterBreak();
        Utils.dropInventoryItems(world, pos, inventory);
    }

    private void spawnItems(ItemStack stack)
    {
        if (!stack.isEmpty()) Utils.spawnItemStack(world, pos.offset(getMasterFacing().getOpposite(), 2), stack);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TEStorageChest;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
            return LazyOptional.of(() -> getMaster().getBottomTE().inventory).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("inv", inventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        inventory.deserializeNBT(compound.getCompound("inv"));
        super.read(compound);
    }
}
