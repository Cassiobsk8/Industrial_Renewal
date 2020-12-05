package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public abstract class BlockBasicElectricFence extends BlockBase
{

    public BlockBasicElectricFence(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(5);
        setResistance(25);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, BlockState state, Entity entityIn)
    {
        if (!(entityIn instanceof EntityLivingBase)) {
            return;
        }

        int mode = IRConfig.MainConfig.Main.electricFenceMode;
        float damage = (float) IRConfig.MainConfig.Main.electricFenceDamageAmount;

        if (mode == 4) return;
        if (mode == 0 && (entityIn instanceof EntityMob || entityIn instanceof PlayerEntity)) {
            float damageR = (entityIn instanceof PlayerEntity) ? 0f : damage;
            DoDamage(worldIn, pos, entityIn, damageR);
        } else if (mode == 1 && (entityIn instanceof EntityMob || entityIn instanceof PlayerEntity)) {
            DoDamage(worldIn, pos, entityIn, damage);
        } else if (mode == 2) {
            DoDamage(worldIn, pos, entityIn, 0f);
        } else if (mode == 3) {
            DoDamage(worldIn, pos, entityIn, damage);
        }
    }

    private void DoDamage(World world, BlockPos pos, Entity entityIn, float amount) {
        entityIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, amount);
        float knockback = (float) IRConfig.MainConfig.Main.electricFenceKnockBack;
        if (knockback > 0)
            ((EntityLivingBase) entityIn).knockBack(entityIn, knockback, pos.getX() - entityIn.posX, pos.getZ() - entityIn.posZ);
        Random r = new Random();
        float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
        world.playSound(null, pos, IRSoundRegister.EFFECT_SHOCK, SoundCategory.BLOCKS, 0.6F * IRConfig.MainConfig.Sounds.masterVolumeMult, pitch);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final  BlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final  BlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
