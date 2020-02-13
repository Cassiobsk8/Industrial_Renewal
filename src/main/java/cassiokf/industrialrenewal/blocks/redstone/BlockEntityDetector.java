package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityEntityDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDetector extends BlockTileEntity<TileEntityEntityDetector>
{

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final DirectionProperty BASE = DirectionProperty.create("base", Direction.values());

    public BlockEntityDetector(Block.Properties property)
    {
        super(property);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));

    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityEntityDetector)
            {
                OpenGUI(worldIn, pos, player);
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        //player.openGui(IndustrialRenewal.instance, GUIHandler.ENTITYDETECTOR, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Nonnull

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntityEntityDetector te = (TileEntityEntityDetector) worldIn.getTileEntity(currentPos);
        return stateIn.with(BASE, te.getBlockFacing());
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        return side != state.get(FACING).getOpposite();
    }


    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }


    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }


    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        boolean active = state.get(ACTIVE);
        if (!active)
        {
            return 0;
        }
        return 15;
    }

    @Nonnull
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, BASE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction theFace;
        if (context.getPlayer().isCrouching())
        {
            theFace = context.getFace().getOpposite();
        } else
        {
            theFace = context.getPlayer().getHorizontalFacing().getOpposite();
        }
        return this.getDefaultState().with(FACING, theFace).with(BASE, context.getFace().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntityEntityDetector te = (TileEntityEntityDetector) worldIn.getTileEntity(pos);
        Direction facing = state.get(BASE);
        te.setBlockFacing(facing);
    }

    @Nullable
    @Override
    public TileEntityEntityDetector createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityEntityDetector();
    }
}