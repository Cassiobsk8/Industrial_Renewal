package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockFireExtinguisher extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0F, 0.25F, 0.5F, 1F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0F, 0.25F, 0.5F, 1F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0F, 0.5F, 0.75D, 1F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0F, 0.5F, 0.75D, 1F, 0);

    public BlockFireExtinguisher(Block.Properties property)
    {
        super(property);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.isBurning())
        {
            player.extinguish();
            worldIn.addParticle(ParticleTypes.SPLASH, player.getPosition().getX(), player.getPosition().getY() + 1F, player.getPosition().getZ(), 0, 1, 0);
            worldIn.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else if (player.isCrouching())
        {
            if (player.inventory.addItemStackToInventory(new ItemStack(ModItems.fireExtinguisher, 1)))
            {
                worldIn.removeBlock(pos, false);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(ModItems.fireExtinguisher);
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        if (state.getActualState(source, pos).get(ONWALL)) {
            switch (state.getActualState(source, pos).get(FACING)) {
                default:
                case NORTH:
                    return NORTH_BLOCK_AABB;
                case SOUTH:
                    return SOUTH_BLOCK_AABB;
                case EAST:
                    return EAST_BLOCK_AABB;
                case WEST:
                    return WEST_BLOCK_AABB;
            }

        } else {
            return BASE_AABB;
        }
    }
*/

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
