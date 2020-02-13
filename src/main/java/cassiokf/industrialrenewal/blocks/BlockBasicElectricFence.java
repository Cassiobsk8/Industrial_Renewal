package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockBasicElectricFence extends BlockBase
{

    public BlockBasicElectricFence(Block.Properties property)
    {
        super(property);
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
        world.playSound(null, pos, IRSoundRegister.EFFECT_SHOCK, SoundCategory.BLOCKS, 0.6F, pitch);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
