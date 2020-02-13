package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBox;
import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBoxConnector;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.tileentity.TileEntityBoxConnector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityFuseBox extends TileEntity implements ICapabilityProvider
{

    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

    public TileEntityFuseBox()
    {
        super(TileEntityRegister.FUSE_BOX);
    }

    private IItemHandler createHandler()
    {
        return new ItemStackHandler(16);
    }

    public void changeActivate()
    {
        BlockState state = getBlockState();
        boolean active = state.get(BlockFuseBox.ACTIVE);
        world.setBlockState(this.pos, state.with(BlockFuseBox.ACTIVE, !active));
        TileEntityBoxConnector te = getTE();
        if (te != null)
        {
            te.passRedstone();
        }
    }

    public void shockPlayer(PlayerEntity player)
    {
        world.playSound(null, pos, IRSoundRegister.EFFECT_SHOCK, SoundCategory.BLOCKS, 1, 1);
        player.closeScreen();
        player.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 8f);
        player.knockBack(player, 0.4f, this.pos.getX() - player.getPosX(), this.pos.getZ() - player.getPosZ());
    }

    public boolean getActive()
    {
        BlockState state = getBlockState();
        return state.get(BlockFuseBox.ACTIVE);
    }

    public IItemHandler getInv()
    {
        return (IItemHandler) inventory;
    }

    public int getPowerOut()
    {
        TileEntityBoxConnector te = getTE();
        if (te != null)
        {
            return te.getSignalWithAllTheMath(getInPower());
        }
        return getInPower();
    }

    public int getInPower()
    {
        TileEntityBoxConnector te = getTE();
        if (te != null)
        {
            return te.getPowerIn();
        }
        return 0;
    }

    private TileEntityBoxConnector getTE()
    {
        int i = 1;
        while (i < 64)
        {
            Block block = world.getBlockState(this.pos.down(i)).getBlock();
            if (block instanceof BlockFuseBoxConnector)
            {
                TileEntityBoxConnector te = (TileEntityBoxConnector) world.getTileEntity(this.pos.down(i));
                return te;
            }
            i++;
        }
        return null;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        inventory.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? inventory.cast()
                : super.getCapability(capability, facing);
    }
}
