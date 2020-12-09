package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockFireExtinguisher extends BlockHorizontalFacing
{
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");

    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(4, 0, 4, 12, 16, 12);
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 0, 4, 8, 16, 12);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(16, 0, 4, 8, 16, 12);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(4, 0, 8, 12, 16, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(4, 0, 0, 12, 16, 8);

    public BlockFireExtinguisher()
    {
        super(Block.Properties.create(Material.IRON));
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
            if (player.inventory.addItemStackToInventory(new ItemStack(ItemsRegistration.FIREEXTINGUISHER.get())))
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
        return new ItemStack(ItemsRegistration.FIREEXTINGUISHER.get());
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        if (state.get(ONWALL))
        {
            switch (state.get(FACING))
            {
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
}
