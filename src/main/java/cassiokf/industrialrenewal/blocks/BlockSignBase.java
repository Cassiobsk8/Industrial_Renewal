package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockSignBase extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");

    public static final ArrayList<Block> signs = new ArrayList<>();

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1D, 0.875D);

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0D, 0.125D, 0.125D, 0.0625D, 0.875D, 0.875D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1D, 0.125D, 0.125D, 0.9375D, 0.875D, 0.875D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.9375D, 0.875D, 0.875D, 1D);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.0625D, 0.875D, 0.875D, 0D);

    public BlockSignBase(Block.Properties property)
    {
        super(property);
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
        return new ItemStack(ModBlocks.signHV.asItem());
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(ONWALL, context.getFace() != Direction.UP);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL);
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
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.sign_base.info")));
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
