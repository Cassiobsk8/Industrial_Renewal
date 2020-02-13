package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TileEntityLocker extends LockableLootTileEntity
{

    private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);

    public TileEntityLocker()
    {
        super(TileEntityRegister.LOCKER);
    }

    public void read(CompoundNBT compound)
    {
        super.read(compound);
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.contains("CustomName", 8))
        {
            this.setCustomName(new StringTextComponent(compound.getString("CustomName")));
        }
    }

    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.getCustomName().getString());
        }

        return compound;
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn)
    {

    }

    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.chestContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return this.hasCustomName() ? this.getCustomName() : new StringTextComponent("tile.industrialrenewal.locker.name");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        fillWithLoot(player.player);
        //return new ChestContainer(player, "minecraft:chest", player);
        return ChestContainer.createGeneric9X3(id, player, this);
    }
}
