package cassiokf.industrialrenewal.blocks.railroad;


import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCrossingRail extends BlockNormalRailBase
{

    private final long PERIOD = 3000L; // Adjust to suit timing
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public BlockCrossingRail(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        super.onMinecartPass(state, world, pos, cart);
        /**Melhorar isso, passar para a locomotiva porque executa só 1 som por vez em todo mapa */
        long thisTime = System.currentTimeMillis();
        if ((thisTime - lastTime) >= PERIOD)
        {
            lastTime = thisTime;
            world.playSound(null, pos, IRSoundRegister.TILEENTITY_TRAINHORN, SoundCategory.BLOCKS, 2F, 1F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.crossing_rail.info")));
    }

    @Override
    public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }
}