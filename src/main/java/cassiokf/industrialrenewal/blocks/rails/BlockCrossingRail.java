package cassiokf.industrialrenewal.blocks.rails;


import cassiokf.industrialrenewal.IRSoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCrossingRail extends BlockNormalRailBase {

    private final long PERIOD = 3000L; // Adjust to suit timing
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public BlockCrossingRail(String name, CreativeTabs tab) {
        super(name, tab);
    }


    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        super.onMinecartPass(world, cart, pos);
        /**Melhorar isso, passar para a locomotiva porque executa só 1 som por vez em todo mapa */
        long thisTime = System.currentTimeMillis();
        if ((thisTime - lastTime) >= PERIOD) {
            lastTime = thisTime;
            world.playSound(null, pos, IRSoundHandler.TILEENTITY_TRAINHORN, SoundCategory.BLOCKS, 2F, 1F);
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("Make the carts honk when they pass");
    }

    @Override
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
        return false;
    }
}