package cassiokf.industrialrenewal.fluids;

import cassiokf.industrialrenewal.init.FluidInit;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;

public class BlockSteam extends FlowingFluidBlock
{
    public BlockSteam()
    {
        super(() -> FluidInit.STEAM, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100F).noDrops());
    }
}
