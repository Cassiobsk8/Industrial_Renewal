package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import cassiokf.industrialrenewal.enums.EnumBulkConveyorType;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityBulkConveyor;
import cassiokf.industrialrenewal.tileentity.TileEntityBulkConveyorHopper;
import cassiokf.industrialrenewal.tileentity.TileEntityBulkConveyorInserter;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityBulkConveyorBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBulkConveyor extends BlockAbstractHorizontalFacing
{
    public static final IntegerProperty MODE = IntegerProperty.create("mode", 0, 2);
    public static final BooleanProperty FRONT = BooleanProperty.create("front");
    public static final BooleanProperty BACK = BooleanProperty.create("back");

    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 8, 0, 16, 16, 8);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 8, 8, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 8, 0, 8, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(8, 8, 0, 16, 16, 16);

    public final EnumBulkConveyorType type;

    public BlockBulkConveyor(EnumBulkConveyorType type)
    {
        super(Block.Properties.create(Material.IRON));
        this.type = type;
    }

    public static double getMotionX(Direction facing)
    {
        return facing == Direction.EAST ? 0.2 : -0.2;
    }

    public static double getMotionZ(Direction facing)
    {
        return facing == Direction.SOUTH ? 0.2 : -0.2;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (type.equals(EnumBulkConveyorType.NORMAL) && entityIn instanceof LivingEntity)
        {
            Direction facing = worldIn.getBlockState(pos).get(FACING);
            if (facing == Direction.NORTH || facing == Direction.SOUTH)
            {
                entityIn.addVelocity(0, 0, getMotionZ(facing));
            } else
            {
                entityIn.addVelocity(getMotionX(facing), 0, 0);
            }
            //entityIn.onGround = false;
            //entityIn.fallDistance = 0;
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && handIn.equals(Hand.MAIN_HAND))
        {
            if (type.equals(EnumBulkConveyorType.NORMAL))
            {
                if (heldItem.getItem().equals(Blocks.HOPPER.asItem()))
                {
                    Direction facing1 = state.get(FACING);
                    worldIn.setBlockState(pos, BlocksRegistration.CONVEYORVHOPPER.get().getDefaultState().with(FACING, facing1));
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return ActionResultType.SUCCESS;
                }
                if (heldItem.getItem().equals(Blocks.DISPENSER.asItem()))
                {
                    Direction facing1 = state.get(FACING);
                    worldIn.setBlockState(pos, BlocksRegistration.CONVEYORVINSERTER.get().getDefaultState().with(FACING, facing1));
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            } else if (heldItem.getItem().equals(ItemsRegistration.SCREWDRIVE.get()))
            {
                if (type.equals(EnumBulkConveyorType.HOPPER))
                {
                    Direction facing1 = state.get(FACING);
                    worldIn.setBlockState(pos, BlocksRegistration.CONVEYORV.get().getDefaultState().with(FACING, facing1), 3);
                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    return ActionResultType.SUCCESS;
                }
                if (type.equals(EnumBulkConveyorType.INSERTER))
                {
                    Direction facing1 = state.get(FACING);
                    worldIn.setBlockState(pos, BlocksRegistration.CONVEYORV.get().getDefaultState().with(FACING, facing1), 3);
                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    return ActionResultType.SUCCESS;
                }
            } else if (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockBulkConveyor)
            {
                Direction face = state.get(FACING);
                int mode = state.get(MODE);
                if (mode == 2 && worldIn.getBlockState(pos.offset(face).down()).getMaterial().isReplaceable())
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos.offset(face).down(), state, 3);
                        if (!player.isCreative()) heldItem.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (worldIn.getBlockState(pos.offset(face)).getMaterial().isReplaceable())
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos.offset(face), state, 3);
                        if (!player.isCreative()) heldItem.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityBulkConveyorBase)
        {
            ((TileEntityBulkConveyorBase) tileentity).dropInventory();
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        if (!type.equals(EnumBulkConveyorType.NORMAL))
        {
            ItemStack itemst = type.equals(EnumBulkConveyorType.HOPPER)
                    ? new ItemStack(Blocks.HOPPER.asItem())
                    : new ItemStack(Blocks.DISPENSER.asItem());
            if (!worldIn.isRemote)
            {
                spawnAsEntity(worldIn, pos, itemst);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MODE, FRONT, BACK);
    }

    private int getMode(IBlockReader world, BlockPos pos, BlockState ownState)
    {
        if (type != EnumBulkConveyorType.NORMAL) return 0;
        Direction facing = ownState.get(FACING);
        BlockState frontState = world.getBlockState(pos.offset(facing));
        BlockState upState = world.getBlockState(pos.offset(facing).up());
        BlockState directUpState = world.getBlockState(pos.up());
        BlockState downState = world.getBlockState(pos.offset(facing).down());
        BlockState backUpState = world.getBlockState(pos.offset(facing.getOpposite()).up());
        BlockState backState = world.getBlockState(pos.offset(facing.getOpposite()));

        //if (frontState.getBlock() instanceof BlockBulkConveyor && frontState.get(FACING) == facing) return 0;
        if ((upState.getBlock() instanceof BlockBulkConveyor && upState.get(FACING).equals(facing)) && !(directUpState.getBlock() instanceof BlockBulkConveyor) && !(frontState.getBlock() instanceof BlockBulkConveyor && frontState.get(FACING).equals(facing)))
            return 1;
        if ((downState.getBlock() instanceof BlockBulkConveyor && downState.get(FACING).equals(facing)
                && backUpState.getBlock() instanceof BlockBulkConveyor && backUpState.get(FACING).equals(facing))
                || (!(backState.getBlock() instanceof BlockBulkConveyor && backState.get(FACING).equals(facing))
                && (backUpState.getBlock() instanceof BlockBulkConveyor && backUpState.get(FACING).equals(facing))))
            return 2;
        return 0;
    }

    private boolean getFront(IBlockReader world, BlockPos pos, BlockState ownState, final int mode)
    {
        if (type.equals(EnumBulkConveyorType.INSERTER)) return false;

        Direction facing = ownState.get(FACING);
        BlockState frontState = world.getBlockState(pos.offset(ownState.get(FACING)));
        BlockState downState = world.getBlockState(pos.offset(facing).down());

        if (mode == 0)
            return !(frontState.getBlock() instanceof BlockBulkConveyor) || (!(frontState.getBlock() instanceof BlockBulkConveyor && frontState.get(FACING).equals(facing)) && downState.getBlock() instanceof BlockBulkConveyor);
        if (mode == 1) return false;
        if (mode == 2)
            return !(frontState.getBlock() instanceof BlockBulkConveyor) && !(downState.getBlock() instanceof BlockBulkConveyor);
        return false;
    }

    private boolean getBack(IBlockReader world, BlockPos pos, BlockState ownState, final int mode)
    {
        Direction facing = ownState.get(FACING);
        BlockState backState = world.getBlockState(pos.offset(facing.getOpposite()));
        BlockState downState = world.getBlockState(pos.offset(facing.getOpposite()).down());

        if (mode == 0)
            return !(backState.getBlock() instanceof BlockBulkConveyor && backState.get(FACING).equals(facing)) && !(downState.getBlock() instanceof BlockBulkConveyor && downState.get(FACING).equals(facing));
        if (mode == 1)
            return !(downState.getBlock() instanceof BlockBulkConveyor && downState.get(FACING).equals(facing)) && !(backState.getBlock() instanceof BlockBulkConveyor && backState.get(FACING).equals(facing));
        if (mode == 2) return false;
        return false;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        int mode = getMode(worldIn, currentPos, stateIn);
        boolean front = getFront(worldIn, currentPos, stateIn, mode);
        boolean back = getBack(worldIn, currentPos, stateIn, mode);
        return stateIn.with(MODE, mode).with(FRONT, front).with(BACK, back);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState stateIn = getDefaultState();
        int mode = getMode(context.getWorld(), context.getPos(), stateIn);
        boolean front = getFront(context.getWorld(), context.getPos(), stateIn, mode);
        boolean back = getBack(context.getWorld(), context.getPos(), stateIn, mode);
        return stateIn.with(FACING, context.getPlayer().getHorizontalFacing())
                .with(MODE, mode).with(FRONT, front).with(BACK, back);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        if (type.equals(EnumBulkConveyorType.NORMAL)) return state.with(FACING, state.get(FACING).rotateY());
        return state;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        VoxelShape FINAL_SHAPE = NULL_SHAPE;
        Direction face = state.get(FACING);
        int mode = state.get(MODE);
        boolean ramp = mode == 1 || mode == 2;
        if (type == EnumBulkConveyorType.NORMAL)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, BASE_AABB);
            if (ramp)
            {
                switch (face)
                {
                    case NORTH:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                    case SOUTH:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                    case WEST:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
                    case EAST:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                }
            }
        } else
        {
            FINAL_SHAPE = FULL_SHAPE;
        }
        return FINAL_SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        if (type == EnumBulkConveyorType.NORMAL) return new TileEntityBulkConveyor();
        if (type == EnumBulkConveyorType.HOPPER) return new TileEntityBulkConveyorHopper();
        return new TileEntityBulkConveyorInserter();
    }
}
