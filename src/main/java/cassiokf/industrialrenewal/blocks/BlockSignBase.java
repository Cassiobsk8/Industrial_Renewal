package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockSignBase extends BlockHorizontalFacing
{
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");

    public static final ArrayList<Block> signs = new ArrayList<>();

    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(2, 0, 2, 14, 16, 14);
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 2, 2, 1, 14, 14);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(15, 2, 2, 16, 14, 14);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(2, 2, 15, 14, 14, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(2, 2, 0, 14, 14, 1);

    public BlockSignBase()
    {
        super(Block.Properties.create(Material.IRON));
        signs.add(this);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ONWALL, false));
    }

    public void changeSign(World world, BlockPos pos)
    {
        BlockState oldState = world.getBlockState(pos);
        Block oldBlock = oldState.getBlock();

        Direction oldFacing = oldState.get(FACING);
        boolean oldOnWall = oldState.get(ONWALL);
        int nextInt = signs.indexOf(oldBlock) + 1;
        if (nextInt > signs.size() - 1)
        {
            nextInt = 0;
        }
        Block nextBlock = signs.get(nextInt);

        world.setBlockState(pos, nextBlock.getDefaultState().with(FACING, oldFacing).with(ONWALL, oldOnWall));
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(BlocksRegistration.SIGNHV_ITEM.get());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).with(ONWALL, context.getFace() != Direction.UP);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL);
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
        } else
        {
            return BASE_AABB;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.sign_base.info")));
    }
}
