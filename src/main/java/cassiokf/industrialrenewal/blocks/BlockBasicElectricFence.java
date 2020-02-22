package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class BlockBasicElectricFence extends BlockAbstractSixWayConnections
{

    public BlockBasicElectricFence(Block.Properties property, int nodeWidth)
    {
        super(property, nodeWidth, 16);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return false;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (!(entityIn instanceof LivingEntity))
        {
            return;
        }

        int mode = IRConfig.Main.electricFenceMode.get();
        float damage = IRConfig.Main.electricFenceDamageAmount.get().floatValue();

        if (mode == 4) return;
        if (mode == 0 && (entityIn instanceof MonsterEntity || entityIn instanceof PlayerEntity))
        {
            float damageR = (entityIn instanceof PlayerEntity) ? 0f : damage;
            DoDamage(worldIn, pos, entityIn, damageR);
        } else if (mode == 1 && (entityIn instanceof MonsterEntity || entityIn instanceof PlayerEntity))
        {
            DoDamage(worldIn, pos, entityIn, damage);
        } else if (mode == 2)
        {
            DoDamage(worldIn, pos, entityIn, 0f);
        } else if (mode == 3)
        {
            DoDamage(worldIn, pos, entityIn, damage);
        }
    }

    private void DoDamage(World world, BlockPos pos, Entity entityIn, float amount)
    {
        entityIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, amount);
        float knockback = IRConfig.Main.electricFenceKnockBack.get().floatValue();
        if (knockback > 0)
            ((LivingEntity) entityIn).knockBack(entityIn, knockback, pos.getX() - entityIn.getPosX(), pos.getZ() - entityIn.getPosZ());
        Random r = new Random();
        float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
        world.playSound(null, pos, SoundsRegistration.EFFECT_SHOCK.get(), SoundCategory.BLOCKS, 0.6F, pitch);
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }
}
