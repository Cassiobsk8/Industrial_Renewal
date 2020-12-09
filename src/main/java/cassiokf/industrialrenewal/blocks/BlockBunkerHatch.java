package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.tileentity.TileEntityBunkerHatch;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBunkerHatch extends BlockMultiBlockBase<TileEntityBunkerHatch>
{
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public BlockBunkerHatch()
    {
        super(Block.Properties.create(Material.IRON));
        setDefaultState(getDefaultState().with(OPEN, false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityBunkerHatch) ((TileEntityBunkerHatch) tile).changeOpen();
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        if (state.get(OPEN))
        {
            return 0;
        } else
        {
            return 250;
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return state.get(OPEN);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return worldIn.getBlockState(pos).get(OPEN);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x1x3Centered(masterPos);
    }

    @Override
    protected BlockPos getMasterPosBasedOnPlace(BlockPos pos, Direction facing)
    {
        return pos.offset(facing);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, OPEN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing())
                .with(MASTER, false).with(OPEN, false);
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        Boolean active = state.get(OPEN);
        if (active)
        {
            return NONE_AABB;
        }
        return FULL_AABB;
    }

    @Nullable
    @Override
    public TileEntityBunkerHatch createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBunkerHatch();
    }
}
