package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockRazorWire extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty FRAME = BooleanProperty.create("frame");

    public BlockRazorWire()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof LivingEntity)
        {
            entityIn.setMotionMultiplier(state, new Vec3d(0.25D, 0.05D, 0.25D));
            entityIn.attackEntityFrom(DamageSource.CACTUS, IRConfig.Main.razorWireDamage.get().floatValue());
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return NULL_SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, FRAME);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        Direction face = stateIn.get(FACING);
        if (facing == face.rotateY()) return stateIn.with(FRAME, canConnect(worldIn, currentPos, stateIn));
        return stateIn;
    }

    private boolean canConnect(IBlockReader world, BlockPos pos, BlockState state)
    {
        Direction facing = state.get(FACING);
        return !(world.getBlockState(pos.offset(facing.rotateY())).getBlock() instanceof BlockRazorWire);
    }
}
