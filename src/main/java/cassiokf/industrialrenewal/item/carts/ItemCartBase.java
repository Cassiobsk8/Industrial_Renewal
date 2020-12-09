package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.item.ItemBase;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

public abstract class ItemCartBase extends ItemBase
{
    public ItemCartBase(Item.Properties properties)
    {
        super(properties.maxStackSize(16));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
         BlockState blockState = context.getWorld().getBlockState(context.getPos());

        if (!AbstractRailBlock.isRail(blockState))
        {
            return ActionResultType.FAIL;
        } else
        {
            ItemStack itemstack = context.getPlayer().getHeldItemMainhand();

            if (!context.getWorld().isRemote)
            {
                RailShape blockrailbase$enumraildirection =  blockState.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock)  blockState.getBlock()).getRailDirection(blockState, context.getWorld(), context.getPos(),null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;

                if (blockrailbase$enumraildirection.isAscending())
                {
                    d0 = 0.5D;
                }

                AbstractMinecartEntity MinecartEntity = getEntity(context.getWorld(), (double) context.getPos().getX() + 0.5D, (double) context.getPos().getY() + 0.0625D + d0, (double) context.getPos().getZ() + 0.5D);

                if (itemstack.hasDisplayName())
                {
                    MinecartEntity.setCustomName(itemstack.getDisplayName());
                }

                context.getWorld().addEntity(MinecartEntity);
            }

            itemstack.shrink(1);
            return ActionResultType.SUCCESS;
        }
    }

    public abstract AbstractMinecartEntity getEntity(World world, double x, double y, double z);
}
