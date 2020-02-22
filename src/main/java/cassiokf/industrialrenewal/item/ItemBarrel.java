package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityBarrel;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ItemBarrel extends ItemBase
{
    public ItemBarrel(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        BlockPos pos = context.getPos();
        ItemStack itemstack = context.getItem();
        BlockPos posOffset = context.getPos().offset(context.getFace());
        World worldIn = context.getWorld();
        BlockState state = worldIn.getBlockState(posOffset);
        if (worldIn.isAirBlock(posOffset) || state.getBlock().isReplaceable(state, new BlockItemUseContext(context)))
        {
            playSound(worldIn, pos, SoundEvents.BLOCK_METAL_PLACE);

            BlockState barrelState = BlocksRegistration.BARREL.get().getStateForPlacement(new BlockItemUseContext(context));
            worldIn.setBlockState(posOffset, barrelState);
            TileEntityBarrel te = (TileEntityBarrel) worldIn.getTileEntity(posOffset);
            if (itemstack.getTag() != null && itemstack.getTag().contains("FluidName") && te != null)
                te.tank.readFromNBT(itemstack.getTag());
            itemstack.shrink(1);

            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        CompoundNBT nbt = itemstack.getTag();
        if (nbt != null && nbt.contains("FluidName") && nbt.contains("Amount"))
        {
            list.add(new StringTextComponent(nbt.getString("FluidName") + ": " + nbt.getInt("Amount")));
        }
    }

    private void playSound(World world, BlockPos pos, SoundEvent soundEvent)
    {
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
