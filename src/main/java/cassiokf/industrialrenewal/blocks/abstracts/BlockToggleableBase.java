package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.enums.enumproperty.EnumFaceRotation;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockToggleableBase<TE extends TileEntityToggleableBase> extends BlockTileEntity<TE>
{

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IProperty<EnumFaceRotation> FACE_ROTATION = EnumProperty.create("face_rotation", EnumFaceRotation.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.125D, 0.875D, 0.875D, 0.875D);

    public BlockToggleableBase(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, FACE_ROTATION);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.getHeldItem(Hand.MAIN_HAND).isEmpty())
        {
            TileEntityToggleableBase te = getTileEntity(worldIn, pos);
            te.playSwitchSound();
            boolean active = !state.get(ACTIVE);
            state = state.with(ACTIVE, active);
            te.setActive(active);
            worldIn.setBlockState(pos, state, 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    public void setFace(World world, BlockPos pos)
    {
        TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        Direction vFace = getFacing(world, pos);
        EnumFaceRotation rFace = getFaceRotation(world, pos);
        if (vFace == Direction.UP || vFace == Direction.DOWN)
        {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN)
            {
                tileEntity.activeFacing(Direction.EAST);
                tileEntity.activeFacing(Direction.WEST);
                tileEntity.disableFacing(Direction.NORTH);
                tileEntity.disableFacing(Direction.SOUTH);
                tileEntity.disableFacing(Direction.UP);
                tileEntity.disableFacing(Direction.DOWN);
            } else
            {
                tileEntity.activeFacing(Direction.SOUTH);
                tileEntity.activeFacing(Direction.NORTH);
                tileEntity.disableFacing(Direction.UP);
                tileEntity.disableFacing(Direction.DOWN);
                tileEntity.disableFacing(Direction.EAST);
                tileEntity.disableFacing(Direction.WEST);
            }
        }
        if (vFace == Direction.NORTH || vFace == Direction.SOUTH)
        {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN)
            {
                tileEntity.activeFacing(Direction.EAST);
                tileEntity.activeFacing(Direction.WEST);
                tileEntity.disableFacing(Direction.NORTH);
                tileEntity.disableFacing(Direction.SOUTH);
                tileEntity.disableFacing(Direction.UP);
                tileEntity.disableFacing(Direction.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT)
            {
                tileEntity.activeFacing(Direction.UP);
                tileEntity.activeFacing(Direction.DOWN);
                tileEntity.disableFacing(Direction.NORTH);
                tileEntity.disableFacing(Direction.SOUTH);
                tileEntity.disableFacing(Direction.EAST);
                tileEntity.disableFacing(Direction.WEST);
            }
        }
        if (vFace == Direction.WEST || vFace == Direction.EAST)
        {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN)
            {
                tileEntity.activeFacing(Direction.NORTH);
                tileEntity.activeFacing(Direction.SOUTH);
                tileEntity.disableFacing(Direction.EAST);
                tileEntity.disableFacing(Direction.WEST);
                tileEntity.disableFacing(Direction.UP);
                tileEntity.disableFacing(Direction.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT)
            {
                tileEntity.activeFacing(Direction.UP);
                tileEntity.activeFacing(Direction.DOWN);
                tileEntity.disableFacing(Direction.NORTH);
                tileEntity.disableFacing(Direction.SOUTH);
                tileEntity.disableFacing(Direction.EAST);
                tileEntity.disableFacing(Direction.WEST);
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
    {
        return BLOCK_AABB;
    }

*/

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, final LivingEntity placer, final ItemStack stack)
    {

        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);

        setFacing(world, pos, placer.getHorizontalFacing());
        setFace(world, pos);

        tileEntity.markDirty();
    }

    public Direction getFacing(final IBlockReader world, final BlockPos pos)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFacing() : Direction.SOUTH;
    }

    public EnumFaceRotation getFaceRotation(final IBlockReader world, final BlockPos pos)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFaceRotation() : EnumFaceRotation.UP;
    }

    public void setFacing(final IBlockReader world, final BlockPos pos, final Direction facing)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        if (tileEntity != null)
        {
            tileEntity.setFacing(facing);
        }
    }

    public void setFaceRotation(final IBlockReader world, final BlockPos pos, final EnumFaceRotation faceRotation)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        if (tileEntity != null)
        {
            tileEntity.setFaceRotation(faceRotation);
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(FACING, getFacing(worldIn, currentPos))
                .with(FACE_ROTATION, getFaceRotation(worldIn, currentPos));
    }

    public void rotateFace(final World world, final BlockPos pos)
    {
        final EnumFaceRotation faceRotation = getFaceRotation(world, pos);
        setFaceRotation(world, pos, faceRotation.rotateClockwise());
    }
}