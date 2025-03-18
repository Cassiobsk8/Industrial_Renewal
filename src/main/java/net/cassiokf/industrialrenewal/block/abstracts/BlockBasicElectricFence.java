package net.cassiokf.industrialrenewal.block.abstracts;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public abstract class BlockBasicElectricFence extends BlockAbstractSixWayConnections {
    
    public BlockBasicElectricFence(Block.Properties property, int nodeWidth) {
        super(property, nodeWidth, 16);
    }
    
    public BlockBasicElectricFence(Block.Properties property, int nodeWidth, int nodeHeight) {
        super(property, nodeWidth, nodeHeight);
    }
    
    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        return false;
    }
    
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        //        Utils.debug("Entity Inside", entityIn.isColliding(pos, state));
        //        if(entityIn.isColliding(pos, state)){
        //            Utils.debug("Entity Colliding");
        //TODO: Add to config
        int mode = 1;
        float damage = 1;
        
        if (mode == 0 && (entityIn instanceof Monster || entityIn instanceof Player)) {
            float damageR = (entityIn instanceof Player) ? 0f : damage;
            DoDamage(worldIn, pos, entityIn, damageR);
        } else if (mode == 1 && (entityIn instanceof Monster || entityIn instanceof Player)) {
            DoDamage(worldIn, pos, entityIn, damage);
        } else if (mode == 2) {
            DoDamage(worldIn, pos, entityIn, 0f);
        } else if (mode == 3) {
            DoDamage(worldIn, pos, entityIn, damage);
        }
        //        }
        super.entityInside(state, worldIn, pos, entityIn);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return super.getCollisionShape(state, worldIn, pos, context);
    }
    
    private void DoDamage(Level world, BlockPos pos, Entity entityIn, float amount) {
        entityIn.hurt(entityIn.damageSources().lightningBolt(), amount);
        //TODO: add to config
        //        float knockback = 0.3F;
        //        if (knockback > 0)
        //            ((LivingEntity) entityIn).knockback(knockback, pos.getX() - entityIn.getX(), pos.getZ() - entityIn.getZ());
        //        Utils.debug("knockback", pos.getX() - entityIn.getX(), pos.getZ() - entityIn.getZ());
        Random r = new Random();
        float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
        //world.playSound(null, pos, SoundsRegistration.EFFECT_SHOCK.get(), SoundCategory.BLOCKS, 0.6F, pitch);
    }
}
