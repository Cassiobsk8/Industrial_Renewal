package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrafficLight extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");
    public static final IntegerProperty SIGNAL = IntegerProperty.create("signal", 0, 2);

    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(4, 0, 4, 12, 30, 12);
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 2, 4, 8, 14, 12);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(8, 2, 4, 16, 14, 12);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(4, 2, 8, 12, 14, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(4, 2, 0, 12, 14, 8);

    public BlockTrafficLight()
    {
        super(Block.Properties.create(Material.IRON).lightValue(7));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ONWALL, false).with(SIGNAL, 0));
    }

    private int getSignal(IWorld world, BlockPos pos, BlockState state)
    {
        World worldIn = world.getWorld();
        BlockPos offsetPos = pos.offset(state.get(BlockTrafficLight.FACING));
        boolean onWall = state.get(ONWALL);
        if (!onWall && (worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.down())))
        {
            int power = Math.max(worldIn.getRedstonePowerFromNeighbors(pos), worldIn.getRedstonePowerFromNeighbors(pos.down()));
            if (power > 3 && power < 10)
            {
                return 1;
            } else if (power >= 10)
            {
                return 2;
            }
        } else if (onWall && (worldIn.isBlockPowered(offsetPos) || worldIn.isBlockPowered(pos)))
        {
            int power = Math.max(worldIn.getStrongPower(offsetPos), worldIn.getStrongPower(pos));
            if (power > 3 && power < 10)
            {
                return 1;
            } else if (power >= 10)
            {
                return 2;
            }
        }
        return 0;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        worldIn.setBlockState(pos, state.with(SIGNAL, getSignal(worldIn, pos, state)));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(SIGNAL, getSignal(worldIn, currentPos, stateIn));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = context.getPlacementHorizontalFacing();
        if (Direction.Plane.HORIZONTAL.test(context.getFace()))
            facing = context.getFace().getOpposite();
        BlockState state = getDefaultState()
                .with(FACING, facing)
                .with(ONWALL, context.getFace() != Direction.UP);
        return state.with(SIGNAL, getSignal(context.getWorld(), context.getPos(), state));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL, SIGNAL);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("block.industrialrenewal.traffic_light.des0")));
        tooltip.add(new StringTextComponent(I18n.format("block.industrialrenewal.traffic_light.des1")));
        tooltip.add(new StringTextComponent(I18n.format("block.industrialrenewal.traffic_light.des2")));
        tooltip.add(new StringTextComponent(I18n.format("block.industrialrenewal.traffic_light.des3")));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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
