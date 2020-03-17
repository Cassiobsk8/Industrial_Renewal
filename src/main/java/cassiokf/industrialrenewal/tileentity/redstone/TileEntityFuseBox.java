package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBox;
import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBoxConnector;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.TileEntityBoxConnector;
import cassiokf.industrialrenewal.tileentity.abstractclass.TEBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityFuseBox extends TEBase
{

    public ItemStackHandler inventory;

    public TileEntityFuseBox()
    {
        this.inventory = new ItemStackHandler(16);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        if ((oldState.getBlock() != newState.getBlock()))
        {
            TileEntityBoxConnector te = getTE();
            if (te != null) {
                te.setActive(false);
            }
        }
        return (oldState.getBlock() != newState.getBlock());
    }

    public void changeActivate() {
        IBlockState state = this.world.getBlockState(this.pos);
        boolean active = state.getValue(BlockFuseBox.ACTIVE);
        this.world.setBlockState(this.pos, state.withProperty(BlockFuseBox.ACTIVE, !active));
        TileEntityBoxConnector te = getTE();
        if (te != null) {
            te.passRedstone();
        }
    }

    public void shockPlayer(EntityPlayer player) {
        this.world.playSound(null, this.pos, IRSoundRegister.EFFECT_SHOCK, SoundCategory.BLOCKS, 1, 1);
        player.closeScreen();
        player.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 8f);
        player.knockBack(player, 0.4f, this.pos.getX() - player.posX, this.pos.getZ() - player.posZ);
    }

    public boolean getActive() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockFuseBox.ACTIVE);
    }

    public ItemStackHandler getInv() {
        return this.inventory;
    }

    public int getPowerOut() {
        TileEntityBoxConnector te = getTE();
        if (te != null) {
            return te.getSignalWithAllTheMath(getInPower());
        }
        return getInPower();
    }

    public int getInPower() {
        TileEntityBoxConnector te = getTE();
        if (te != null) {
            return te.getPowerIn();
        }
        return 0;
    }

    private TileEntityBoxConnector getTE() {
        int i = 1;
        while (i < 64) {
            Block block = this.world.getBlockState(this.pos.down(i)).getBlock();
            if (block instanceof BlockFuseBoxConnector) {
                TileEntityBoxConnector te = (TileEntityBoxConnector) this.world.getTileEntity(this.pos.down(i));
                return te;
            }
            i++;
        }
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", this.inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
    }
}
