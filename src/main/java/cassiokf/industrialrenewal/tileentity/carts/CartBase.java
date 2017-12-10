package cassiokf.industrialrenewal.tileentity.carts;

/**import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;



public abstract class CartBase extends EntityMinecart extends TileEntity {

    protected String name;
    public void CartBase(String name) {

    }

    protected CartBase(World world) {
        super(world);
    }

    public boolean doInteract(EntityPlayer player) {
        return true;
    }
    @Override
    public boolean canBeRidden() {
        return false;
    }
    @Override
    public boolean isPoweredCart() {
        return false;
    }
} */