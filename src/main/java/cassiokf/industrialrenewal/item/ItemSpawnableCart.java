package cassiokf.industrialrenewal.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemSpawnableCart extends ItemBase
{


    public ItemSpawnableCart(Properties properties)
    {
        super(properties.maxStackSize(16));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState iblockstate = worldIn.getBlockState(pos);

        if (!(iblockstate.getBlock() instanceof RailBlock))
        {
            return ActionResultType.FAIL;
        } else
        {
            ItemStack itemstack = context.getItem();

            if (!worldIn.isRemote)
            {
                RailShape blockrailbase$enumraildirection = iblockstate.getBlock() instanceof RailBlock
                        ? ((RailBlock) iblockstate.getBlock()).getRailDirection(iblockstate, worldIn, pos, null)
                        : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;

                if (blockrailbase$enumraildirection.isAscending())
                {
                    d0 = 0.5D;
                }

                AbstractMinecartEntity entityminecart = getMinecartEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);

                if (itemstack.hasDisplayName())
                {
                    entityminecart.setCustomName(itemstack.getDisplayName());
                }

                worldIn.addEntity(entityminecart);
            }

            itemstack.shrink(1);
            return ActionResultType.SUCCESS;
        }
    }

    public abstract AbstractMinecartEntity getMinecartEntity(World world, double x, double y, double z);

}
