package cassiokf.industrialrenewal.blocks.railroad;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBoosterRail extends PoweredRailBlock
{
    public BlockBoosterRail()
    {
        super(Block.Properties.create(Material.IRON).notSolid(), true);
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        super.onMinecartPass(state, world, pos, cart);
    }
}