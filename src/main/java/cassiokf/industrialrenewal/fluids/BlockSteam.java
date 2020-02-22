package cassiokf.industrialrenewal.fluids;

import cassiokf.industrialrenewal.init.FluidsRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSteam extends FlowingFluidBlock
{
    public BlockSteam()
    {
        super(() -> FluidsRegistration.STEAM.get(), Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100F).noDrops());
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof LivingEntity)
        {
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 0.5F);
        }
    }
}
