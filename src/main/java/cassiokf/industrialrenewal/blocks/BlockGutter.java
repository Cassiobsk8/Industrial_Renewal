package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityGutter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockGutter extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty ACTIVE_LEFT = BooleanProperty.create("active_left");
    public static final BooleanProperty ACTIVE_RIGHT = BooleanProperty.create("active_right");
    public static final BooleanProperty ACTIVE_DOWN = BooleanProperty.create("active_down");

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 1.0D, 0.75D, 1.0D);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 1.0D, 0.75D, 0.3125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.6875D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 0.3125D, 0.75D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.6875D, 0.4375D, 0.0D, 1.0D, 0.75D, 1.0D);

    protected static final AxisAlignedBB NC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 0.8125D);
    protected static final AxisAlignedBB SC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB WC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.75D, 1.0D);
    protected static final AxisAlignedBB EC_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockGutter()
    {
        super(Block.Properties.create(Material.IRON));
        setDefaultState(getDefaultState().with(ACTIVE_DOWN, false));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.produces")
                + ": "));
        tooltip.add(new StringTextComponent(Blocks.WATER.getTranslationKey()
                + (" 1")
                + " mB/t "
                + I18n.format("info.industrialrenewal.gutter")));
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.gutter2")));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private Boolean downConnected(IBlockReader world, BlockPos pos)
    {
        final TileEntity tileEntityS = world.getTileEntity(pos.offset(Direction.DOWN));
        return tileEntityS != null && !tileEntityS.isRemoved() && tileEntityS.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).isPresent();
    }

    private Boolean leftConnected(BlockState state, IBlockReader world, BlockPos pos)
    {
        Direction face = state.get(FACING);
        Block block = world.getBlockState(pos.offset(face.rotateY().getOpposite())).getBlock();
        if (block instanceof BlockGutter)
        {
            Direction leftFace = world.getBlockState(pos.offset(face.rotateY().getOpposite())).get(FACING);
            return !(leftFace == face);
        }
        return true;
    }

    private Boolean rightConnected(BlockState state, IBlockReader world, BlockPos pos)
    {
        Direction face = state.get(FACING);
        Block block = world.getBlockState(pos.offset(face.rotateY())).getBlock();
        if (block instanceof BlockGutter)
        {
            Direction rightFace = world.getBlockState(pos.offset(face.rotateY())).get(FACING);
            return !(rightFace == face);
        }
        return true;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn
                .with(ACTIVE_DOWN, downConnected(worldIn, currentPos))
                .with(ACTIVE_LEFT, leftConnected(stateIn, worldIn, currentPos))
                .with(ACTIVE_RIGHT, rightConnected(stateIn, worldIn, currentPos));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE_LEFT, ACTIVE_RIGHT, ACTIVE_DOWN);
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            BlockState actualState = state.getActualState(worldIn, pos);
            Direction face = actualState.get(FACING);
            Boolean active = actualState.get(ACTIVE_DOWN);
            if (face == Direction.NORTH) {
                if (active) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
                } else {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                }

            }
            if (face == Direction.SOUTH) {
                if (active) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
                } else {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                }
            }
            if (face == Direction.WEST) {
                if (active) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
                } else {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                }
            }
            if (face == Direction.EAST) {
                if (active) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
                } else {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                }
            }
        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            BlockState actualState = state.getActualState(source, pos);
            Direction face = actualState.get(FACING);
            Boolean active = actualState.get(ACTIVE_DOWN);
            if (face == Direction.NORTH) {
                if (active) {
                    return NC_AABB;
                } else {
                    return NORTH_AABB;
                }

            }
            if (face == Direction.SOUTH) {
                if (active) {
                    return SC_AABB;
                } else {
                    return SOUTH_AABB;
                }
            }
            if (face == Direction.WEST) {
                if (active) {
                    return WC_AABB;
                } else {
                    return WEST_AABB;
                }
            }
            if (face == Direction.EAST) {
                if (active) {
                    return EC_AABB;
                } else {
                    return EAST_AABB;
                }
            }
            return null;
        }
    */

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityGutter createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityGutter();
    }
}
