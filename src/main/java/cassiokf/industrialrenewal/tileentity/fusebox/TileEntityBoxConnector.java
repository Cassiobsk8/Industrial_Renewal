package cassiokf.industrialrenewal.tileentity.fusebox;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityBoxConnector extends TileEntity {

    private boolean active;

    public TileEntityBoxConnector() {
    }

    public int passRedstone() {
        IBlockState state = this.world.getBlockState(this.pos);
        EnumFacing inFace = state.getValue(BlockFuseBoxConnector.FACING).rotateYCCW();
        IBlockState neightbor = world.getBlockState(pos.offset(inFace));
        Block block = neightbor.getBlock();
        if (getActive()) {
            if (block == Blocks.REDSTONE_BLOCK || block == Blocks.REDSTONE_TORCH) {
                return 15;
            } else {
                return block == Blocks.REDSTONE_WIRE ? neightbor.getValue(BlockRedstoneWire.POWER) : this.world.getStrongPower(this.pos.offset(inFace), inFace);
            }
        }
        return 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("active", active);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        active = compound.getBoolean("active");
        super.readFromNBT(compound);
    }

    public void setActive(boolean value) {
        active = value;
        this.world.notifyNeighborsOfStateChange(this.pos, this.world.getBlockState(this.pos).getBlock(), true);
    }


    public boolean getActive() {
        return active;
    }
}
