package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBookManual extends ItemBase {

    public ItemBookManual(String name, CreativeTabs tab) {
        super(name, tab);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, EnumHand handIn) {
        OpenGUI(worldIn, playerIn.getPosition(), playerIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player) {
        world.playSound(null, player.getPosition(), IRSoundRegister.BOOK_FLIP, SoundCategory.BLOCKS, 1f, 1f);
        player.openGui(IndustrialRenewal.instance, GUIHandler.MANUAL, world, pos.getX(), pos.getY(), pos.getZ());
    }
}
