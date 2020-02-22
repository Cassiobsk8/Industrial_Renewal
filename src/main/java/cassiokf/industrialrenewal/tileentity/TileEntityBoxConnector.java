package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBox;
import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBoxConnector;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFuseBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static cassiokf.industrialrenewal.init.TileRegistration.FUSEBOXCONNECTOR_TILE;

public class TileEntityBoxConnector extends TileEntity
{

    private boolean active;

    public TileEntityBoxConnector()
    {
        super(FUSEBOXCONNECTOR_TILE.get());
    }

    public int passRedstone()
    {
        int value;
        if (getActive())
        {
            value = getSignalWithAllTheMath(getPowerIn());
        } else
        {
            value = 0;
        }
        return value;
    }

    public int getSignalWithAllTheMath(int input)
    {
        IItemHandler inv = getInventory();
        if (inv != null)
        {
            for (int i = 0; i < inv.getSlots(); i++)
            {
                Item item = inv.getStackInSlot(i).getItem();
                if (item == ItemsRegistration.CARTRIDGE_MINUS.get())
                {
                    input--;
                }
                if (item == ItemsRegistration.CARTRIDGE_PLUS.get())
                {
                    input++;
                }
                if (item == ItemsRegistration.CARTRIDGE_HALF.get())
                {
                    input = input / 2;
                }
                if (item == ItemsRegistration.CARTRIDGE_DOUBLE.get())
                {
                    input = input * 2;
                }
                if (item == ItemsRegistration.CARTRIDGE_INVERTER.get())
                {
                    if (input > 0)
                    {
                        input = 0;
                    } else
                    {
                        input = 15;
                    }
                }
            }
        }
        if (input > 15)
        {
            input = 15;
        }
        if (input < 0)
        {
            input = 0;
        }
        return input;
    }

    private IItemHandler getInventory()
    {
        TileEntityFuseBox te = getTE();
        if (te != null)
        {
            return te.getInv();
        }
        return null;
    }

    public int getPowerIn()
    {
        BlockState state = getBlockState();
        Direction inFace = state.get(BlockFuseBoxConnector.FACING).rotateYCCW();
        BlockState neighbor = world.getBlockState(pos.offset(inFace));
        Block block = neighbor.getBlock();
        if (block == Blocks.REDSTONE_BLOCK || block == Blocks.REDSTONE_TORCH)
        {
            return 15;
        } else
        {
            return block == Blocks.REDSTONE_WIRE ? neighbor.get(RedstoneWireBlock.POWER) : world.getStrongPower(this.pos.offset(inFace), inFace);
        }
    }

    private void notifyChange()
    {
        world.notifyNeighborsOfStateChange(this.pos, getBlockState().getBlock());
        markDirty();
    }

    public boolean getActive()
    {
        TileEntityFuseBox te = getTE();
        if (te != null)
        {
            boolean nActive = te.getActive();
            if (nActive != active)
            {
                setActive(nActive);
            }
        }
        return active;
    }

    public void setActive(boolean value)
    {
        active = value;
        notifyChange();
    }

    private TileEntityFuseBox getTE()
    {
        int i = 1;
        while (i < 64)
        {
            Block block = world.getBlockState(this.pos.up(i)).getBlock();
            if (block instanceof BlockFuseBox)
            {
                return (TileEntityFuseBox) world.getTileEntity(this.pos.up(i));
            }
            i++;
        }
        return null;
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("active", active);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        active = compound.getBoolean("active");
        super.read(compound);
    }
}
