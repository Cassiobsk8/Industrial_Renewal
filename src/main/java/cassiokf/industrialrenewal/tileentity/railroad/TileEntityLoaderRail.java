package cassiokf.industrialrenewal.tileentity.railroad;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLoaderRail extends TileEntity {

    private boolean canLetPass;

    private EntityMinecart cart;

    public void ChangePass(boolean value) {
        canLetPass = value;
        markDirty();
    }

    public boolean GetPass() {
        return canLetPass;
    }

    public EntityMinecart GetCart() {
        return cart;
    }

    public void SetCart(EntityMinecart entityMinecart) {
        cart = entityMinecart;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("letitpass", canLetPass);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("letitpass")) canLetPass = compound.getBoolean("letitpass");
        super.readFromNBT(compound);
    }

}
