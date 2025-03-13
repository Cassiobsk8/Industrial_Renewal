package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractSixWayConnections;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockScaffold extends BlockAbstractSixWayConnections {

    protected static final VoxelShape CBASE_AABB = Block.box(1, 0, 1, 15, 16, 15);

    public BlockScaffold() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f), 16, 16);
    }

    @Override
    public boolean canBeReplaced(BlockState p_196253_1_, BlockPlaceContext context) {
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() == this.asItem();
        return super.canBeReplaced(p_196253_1_, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState downState = worldIn.getBlockState(pos.below());
//        if(downState.isFaceSturdy(worldIn, pos.below(), Direction.UP)
//                || downState.getBlock() instanceof BlockScaffold)
        return super.getStateForPlacement(context);
    }

    protected boolean isValidConnection(final BlockState neighborState, final BlockGetter world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        Block nbd = world.getBlockState(ownPos.relative(neighborDirection).below()).getBlock();
        BlockState upState = world.getBlockState(ownPos.above());
        if (neighborDirection == Direction.DOWN)
        {
            return neighborState.isFaceSturdy(world, ownPos.below(), Direction.UP);
        }
        if (neighborDirection != Direction.UP)
        {
            return !(upState.getBlock() instanceof BlockScaffold)
                    && !(upState.isFaceSturdy(world, ownPos.above(), Direction.DOWN))
                    && !(nb instanceof BlockScaffold)
                    && !(nbd instanceof BlockScaffold);
        }
        return !(neighborState.isFaceSturdy(world, ownPos.above(), Direction.DOWN) || nb instanceof BlockScaffold);
    }

    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        return isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    /*
    The Following code, ln 91 - 118, is adapted from
    Immersive Engineering 1.16.5
    /common/blocks/IEBaseBlock.java ln 334 - 361
     */
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn)
    {
        super.entityInside(state, worldIn, pos, entityIn);
        if(entityIn instanceof LivingEntity&&isLadder(state, worldIn, pos, (LivingEntity)entityIn))
            applyLadderLogic(entityIn);
            //entityIn.setDeltaMovement(handleOnClimbable((LivingEntity)entityIn, entityIn.getDeltaMovement()));
//        Utils.debug("INSIDE", entityIn.getDeltaMovement());
    }

    public static void applyLadderLogic(Entity entityIn)
    {
        if(entityIn instanceof LivingEntity&&!((LivingEntity)entityIn).onClimbable())
        {
            Vec3 motion = entityIn.getDeltaMovement();
            float maxMotion = 0.15F;
            motion = new Vec3(
                    Mth.clamp(motion.x, -maxMotion, maxMotion),
                    //Math.max(motion.y, -maxMotion),
                    Mth.clamp(motion.y, -maxMotion, maxMotion),
                    Mth.clamp(motion.z, -maxMotion, maxMotion)
            );

            entityIn.fallDistance = 0.0F;

            if(motion.y < 0 && entityIn instanceof Player && entityIn.isCrouching()) {
                motion = new Vec3(motion.x, 0, motion.z);
                //Utils.debug("CROUCHING", motion);
            }
            else if(entityIn.horizontalCollision) {
                motion = new Vec3(motion.x, 0.2, motion.z);
                //Utils.debug("SLIDING", motion);
            }
            //Utils.debug("FINAL", motion);
            entityIn.setDeltaMovement(motion);
//            Utils.debug("RESULT", entityIn.getDeltaMovement());
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return CBASE_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return CBASE_AABB;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, 2);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }
}
