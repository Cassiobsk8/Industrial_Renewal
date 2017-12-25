package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.tileentity.carts.TileEntityCartCargoContainer;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemMineCartCargoContainer extends ItemBase {

    public int minecartType;
    private int type;

    public ItemMineCartCargoContainer(String name, int cartType) {
        super(name);
        this.type = cartType;
        this.maxStackSize = 16;
        this.minecartType = -1;

    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = world.getBlockState(pos);

        if (!BlockRailBase.isRailBlock(iblockstate))
            return EnumActionResult.FAIL;

        else {
            if (!world.isRemote) {
                pos.offset(EnumFacing.EAST, (int) 0.5);
                int rand = new Random().nextInt(9);
                ItemStack itemstack = player.getHeldItem(hand);
                switch (type) {/*
                case(0):
                {
                    EntityCartOpen entityminecart = new EntityCartOpen(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }
                case(1):
                {
                    EntityCartTanker entityminecart = new EntityCartTanker(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }
                case(2):
                {
                    EntityCartWood entityminecart = new EntityCartWood(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }
                case(3):
                {
                    EntityCartFlat entityminecart = new EntityCartFlat(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }
                case(4):
                {
                    EntityCartPanzer entityminecart = new EntityCartPanzer(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }*/
                    case (5): {
                        TileEntityCartCargoContainer entityminecart = new TileEntityCartCargoContainer(world, pos, rand);
                        if (itemstack.hasDisplayName()) {
                            entityminecart.setCustomNameTag(itemstack.getDisplayName());
                        }
                        world.spawnEntity(entityminecart);
                        if (entityminecart != null) {
                            itemstack.shrink(1);
                        }
                        return EnumActionResult.SUCCESS;
                    }/*
                case(6):
                {
                    EntityCartTender entityminecart = new EntityCartTender(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }

                case(7):
                {
                    EntityCartCage entityminecart = new EntityCartCage(world, pos, rand);
                    if (item.hasDisplayName())
                    {
                        entityminecart.setCustomNameTag(item.getDisplayName());
                    }
                    world.spawnEntityInWorld(entityminecart);
                    if (entityminecart != null)
                    {
                        item.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }*/
                    default: {
                        return EnumActionResult.FAIL;
                    }
                }
            }
        }
        return EnumActionResult.FAIL;
    }

}
