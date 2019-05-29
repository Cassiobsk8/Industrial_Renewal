package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.Fluid.barrel.TileEntityBarrel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

public class ItemBarrel extends ItemBase
{
    public ItemBarrel(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos posOffset = pos.offset(facing);

        if (worldIn.isAirBlock(posOffset) || worldIn.getBlockState(posOffset).getBlock().isReplaceable(worldIn, posOffset))
        {
            playSound(worldIn, pos, "block.metal.place");

            worldIn.setBlockState(posOffset, ModBlocks.barrel.getStateForPlacement(worldIn, posOffset, facing, 0, 0, 0, 0, player));
            TileEntityBarrel te = (TileEntityBarrel) worldIn.getTileEntity(posOffset);
            if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("FluidName") && te != null)
                te.tank.readFromNBT(itemstack.getTagCompound());
            itemstack.shrink(1);

            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag)
    {
        NBTTagCompound nbt = itemstack.getTagCompound();
        if (nbt != null && nbt.hasKey("FluidName") && nbt.hasKey("Amount"))
        {

            list.add(nbt.getString("FluidName") + ": " + nbt.getInteger("Amount"));
        }
    }

    private void playSound(World world, BlockPos pos, String resourceLocation)
    {
        world.playSound(null, pos, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation((resourceLocation)))), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
