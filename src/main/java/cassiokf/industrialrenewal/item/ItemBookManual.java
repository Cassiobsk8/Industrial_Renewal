package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBookManual extends ItemBase
{
    public ItemBookManual(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        OpenGUI(worldIn, playerIn.getPosition(), playerIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        world.playSound(null, player.getPosition(), SoundsRegistration.BOOK_FLIP, SoundCategory.BLOCKS, 1f, 1f);
        player.openGui(IndustrialRenewal.instance, GUIHandler.MANUAL, world, pos.getX(), pos.getY(), pos.getZ());
    }
}
