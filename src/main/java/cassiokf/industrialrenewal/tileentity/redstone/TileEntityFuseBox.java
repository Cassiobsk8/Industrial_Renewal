package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFuseBox;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityBoxConnector;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityFuseBox extends TEBase
{

    public CustomItemStackHandler inventory = new CustomItemStackHandler(16)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityFuseBox.this.markDirty();
        }
    };

    public TileEntityFuseBox(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public void changeActivate() {
        BlockState state = this.world.getBlockState(this.pos);
        boolean active = state.get(BlockFuseBox.ACTIVE);
        this.world.setBlockState(this.pos, state.with(BlockFuseBox.ACTIVE, !active));
        TileEntityBoxConnector te = getTE();
        if (te != null) {
            te.passRedstone();
        }
    }

    public void shockPlayer(PlayerEntity player) {
        this.world.playSound(null, this.pos, SoundsRegistration.EFFECT_SHOCK, SoundCategory.BLOCKS, 1f * IRConfig.Sounds.masterVolumeMult.get(), 1);
        player.closeScreen();
        player.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 8f);
        player.knockBack(player, 0.4f, this.pos.getX() - player.getPosX(), this.pos.getZ() - player.getPosZ());
    }

    public boolean getActive() {
        BlockState state = getBlockState();
        return state.get(BlockFuseBox.ACTIVE);
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
            TileEntity te = this.world.getTileEntity(this.pos.down(i));
            if (te instanceof TileEntityBoxConnector) return (TileEntityBoxConnector) te;
            i++;
        }
        return null;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", this.inventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.inventory.deserializeNBT(compound.getCompound("inventory"));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? LazyOptional.of(() -> inventory).cast()
                : super.getCapability(capability, facing);
    }
}
