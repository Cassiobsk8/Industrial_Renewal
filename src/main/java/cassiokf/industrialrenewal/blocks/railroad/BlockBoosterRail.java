package cassiokf.industrialrenewal.blocks.railroad;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBoosterRail extends PoweredRailBlock
{
    public BlockBoosterRail(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        double d15 = Math.sqrt(cart.getMotion().x * cart.getMotion().x + cart.getMotion().z * cart.getMotion().z);
        /**TODO Precisa desacelerar **/
        if (!world.getBlockState(pos).get(PoweredRailBlock.POWERED))
        {
            return;
        }

        if (d15 > 0.01D)
        {
            cart.addVelocity(cart.getMotion().x / d15 * 0.06D, 0, cart.getMotion().z / d15 * 0.06D);
        }
    }
}