package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTransformerHV extends BlockMultiBlockBase<TileEntityTransformerHV>
{
    public static final IntegerProperty OUTPUT = IntegerProperty.create("output", 0, 2);

    protected static final VoxelShape DOWN_AABB = makeCuboidShape(5, 0, 5, 11, 8, 11);

    public BlockTransformerHV()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(masterPos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, OUTPUT);
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityTransformerHV && pos.equals(((TileEntityTransformerHV) te).getConnectorPos()))
            return DOWN_AABB;

        return FULL_AABB;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntity te = worldIn.getTileEntity(currentPos);
        int type;
        if (!(te instanceof TileEntityTransformerHV) || !((TileEntityTransformerHV) te).isMaster()) type = 0;
        else type = (((TileEntityTransformerHV) te).isOutPut) ? 2 : 1;
        return stateIn.with(OUTPUT, type);
    }

    @Nullable
    @Override
    public TileEntityTransformerHV createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTransformerHV();
    }
}
