package cassiokf.industrialrenewal.tileentity.railroad.railloader;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLoaderRail extends TileEntity {

    private EntityMinecart cart;

    public void setEntityCart(EntityMinecart eCart) {
        cart = eCart;
    }

    public EntityMinecart getEntityCart() {
        return cart;
    }

    public void setLetPass(boolean value) {
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.setBlockState(this.pos, state.withProperty(BlockLoaderRail.PASS, value));
    }

}
