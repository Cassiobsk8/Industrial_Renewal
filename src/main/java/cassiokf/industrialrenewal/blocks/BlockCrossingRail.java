package cassiokf.industrialrenewal.blocks;


import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.block.BlockRail;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCrossingRail extends BlockRail {

    private final long PERIOD = 3000L; // Adjust to suit timing
    protected String name;
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public BlockCrossingRail(String name, CreativeTabs tab) {
        this.name = name;
        setRegistryName(References.MODID, name);
        setUnlocalizedName(References.MODID + "." + name);
        setHardness(0.8f);
        setSoundType(SoundType.WOOD);
        setCreativeTab(tab);
    }


    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
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

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public void registerItemModel(Item itemBlock) {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}