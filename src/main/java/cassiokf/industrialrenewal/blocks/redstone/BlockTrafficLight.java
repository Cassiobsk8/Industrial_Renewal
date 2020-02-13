package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityTrafficLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrafficLight extends BlockTileEntity<TileEntityTrafficLight>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");
    public static final IntegerProperty SIGNAL = IntegerProperty.create("signal", 0, 2);

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.875D, 0.75D);

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.25F, 0.5F, 0.875F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.25F, 0.5F, 0.875F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125F, 0.5F, 0.75D, 0.875F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125F, 0.5F, 0.75D, 0.875F, 0);

    public BlockTrafficLight(Block.Properties property)
    {
        super(property.lightValue(7));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ONWALL, false).with(SIGNAL, 0));
    }

    private int getSignal(IBlockReader world, BlockPos pos)
    {
        TileEntityTrafficLight te = (TileEntityTrafficLight) world.getTileEntity(pos);
        return te.active();
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(SIGNAL, getSignal(worldIn, currentPos));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing())
                .with(ONWALL, context.getFace() != Direction.UP);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL, SIGNAL);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.traffic_light.des0")));
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.traffic_light.des1")));
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.traffic_light.des2")));
        tooltip.add(new StringTextComponent(I18n.format("tile.industrialrenewal.traffic_light.des3")));
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        if (state.get(ONWALL)) {
            switch (state.get(FACING)) {
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

    @Nullable
    @Override
    public TileEntityTrafficLight createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTrafficLight();
    }
}
