package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTransformerHV extends Block3x3Top1Base<TileEntityTransformerHV>
{
    public static final IntegerProperty OUTPUT = IntegerProperty.create("output", 0, 2);

    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);

    public BlockTransformerHV()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, OUTPUT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityTransformerHV && pos.equals(((TileEntityTransformerHV) te).getConnectorPos()))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
        } else
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
    {
        TileEntity te = source.getTileEntity(pos);
        if (te instanceof TileEntityTransformerHV && pos.equals(((TileEntityTransformerHV) te).getConnectorPos()))
        {
            return DOWN_AABB;
        } else
        {
            return FULL_BLOCK_AABB;
        }

    }
*/

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntityTransformerHV te = (TileEntityTransformerHV) worldIn.getTileEntity(currentPos);
        int type;
        if (te == null || !te.isMaster()) type = 0;
        else type = (te.isOutPut) ? 2 : 1;
        return stateIn.with(OUTPUT, type);
    }

    @Nullable
    @Override
    public TileEntityTransformerHV createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTransformerHV();
    }
}
