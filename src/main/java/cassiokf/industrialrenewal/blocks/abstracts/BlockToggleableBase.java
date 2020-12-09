package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.enums.enumproperty.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockToggleableBase<TE extends TileEntityToggleableBase> extends BlockHorizontalFacing
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    public static final IProperty<EnumFaceRotation> FACE_ROTATION = EnumProperty.create("face_rotation", EnumFaceRotation.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockToggleableBase(Block.Properties properties)
    {
        super(properties.hardnessAndResistance(3, 5));
        this.setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityToggleableBase && (flag || blockIn.getDefaultState().canProvidePower()) && flag != ((TileEntityToggleableBase) te).powered)
        {
            ((TileEntityToggleableBase) te).setPowered(flag);
            if (flag != state.get(ACTIVE))
            {
                ((TileEntityToggleableBase) te).setActive(flag);
                worldIn.setBlockState(pos, state.with(ACTIVE, flag), 3);
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, FACE_ROTATION);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        TileEntityToggleableBase te = (TileEntityToggleableBase) worldIn.getTileEntity(pos);
        if (te == null) return ActionResultType.PASS;
        te.playSwitchSound();
        boolean active = !state.get(ACTIVE);
        state = state.with(ACTIVE, active);
        te.setActive(active);
        worldIn.setBlockState(pos, state, 3);
        worldIn.notifyNeighborsOfStateChange(pos, this);
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        rotateFace(world, pos);
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(worldIn, pos);
        setFacing(worldIn, pos, Direction.getFacingDirections(placer)[0]);
        tileEntity.markDirty();
    }

    public Direction getFacing(final IWorld world, final BlockPos pos)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFacing() : Direction.SOUTH;
    }

    public EnumFaceRotation getFaceRotation(final IWorld world, final BlockPos pos)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFaceRotation() : EnumFaceRotation.UP;
    }

    public void setFacing(final IWorld world, final BlockPos pos, final Direction facing)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        if (tileEntity != null)
        {
            tileEntity.setFacing(facing);
        }
    }

    public void setFaceRotation(final IWorld world, final BlockPos pos, final EnumFaceRotation faceRotation)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        if (tileEntity != null)
        {
            tileEntity.setFaceRotation(faceRotation);
        }
    }

    private TileEntityToggleableBase getTileEntity(IWorld world, BlockPos pos)
    {
        return (TileEntityToggleableBase) world.getTileEntity(pos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(FACING, getFacing(worldIn, currentPos))
                .with(FACE_ROTATION, getFaceRotation(worldIn, currentPos));
    }

    public void rotateFace(final IWorld world, final BlockPos pos)
    {
        final EnumFaceRotation faceRotation = getFaceRotation(world, pos);
        setFaceRotation(world, pos, faceRotation.rotateClockwise());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(BlockState state, IBlockReader world);
}