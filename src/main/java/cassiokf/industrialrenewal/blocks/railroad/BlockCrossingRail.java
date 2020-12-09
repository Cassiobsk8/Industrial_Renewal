package cassiokf.industrialrenewal.blocks.railroad;


import cassiokf.industrialrenewal.entity.LocomotiveBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCrossingRail extends BlockNormalRailBase
{

    private static final long PERIOD = 3000L; // Adjust to suit timing
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public BlockCrossingRail()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        super.onMinecartPass(state, world, pos, cart);
        if (cart instanceof LocomotiveBase)
        {
            long thisTime = System.currentTimeMillis();
            if ((thisTime - lastTime) >= PERIOD)
            {
                lastTime = thisTime;
                ((LocomotiveBase) cart).horn();
            }
        }
    }

    @Override
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