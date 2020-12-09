package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacingMultiblocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class BlockPipeBase<TE extends TileEntityMultiBlocksTube> extends BlockTEHorizontalFacingMultiblocks<TE>
{
    public final float nodeWidth;
    public final float nodeHeight;

    public BlockPipeBase(Block.Properties properties, float nodeWidth, float nodeHeight)
    {
        super(properties.hardnessAndResistance(2f, 15f));
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState();
    }

    public boolean isMaster(IWorld world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityMultiBlocksTube && ((TileEntityMultiBlocksTube<?>) te).isMaster();
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        float NORTHZ1;
        if (isConnected(worldIn, pos, TETubeBase.NORTH) || isConnected(worldIn, pos, TETubeBase.CNORTH))
        {
            NORTHZ1 = 0;
        }
        else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        float SOUTHZ2;
        if (isConnected(worldIn, pos, TETubeBase.SOUTH) || isConnected(worldIn, pos, TETubeBase.CSOUTH))
        {
            SOUTHZ2 = 16;
        }
        else
        {
            SOUTHZ2 = 8 + (nodeWidth / 2);
        }
        float WESTX1;
        if (isConnected(worldIn, pos, TETubeBase.WEST) || isConnected(worldIn, pos, TETubeBase.CWEST))
        {
            WESTX1 = 0;
        }
        else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        float EASTX2;
        if (isConnected(worldIn, pos, TETubeBase.EAST) || isConnected(worldIn, pos, TETubeBase.CEAST))
        {
            EASTX2 = 16;
        }
        else
        {
            EASTX2 = 8 + (nodeWidth / 2);
        }
        double DOWN1;
        if (isConnected(worldIn, pos, TETubeBase.DOWN) || isConnected(worldIn, pos, TETubeBase.CDOWN))
        {
            DOWN1 = 0;
        }
        else
        {
            DOWN1 = 8 - (nodeWidth / 2);
        }
        double UP2;
        if (isConnected(worldIn, pos, TETubeBase.UP) || isConnected(worldIn, pos, TETubeBase.CUP))
        {
            UP2 = 16;
        }
        else
        {
            UP2 = 8 + (nodeWidth / 2);
        }
        return Block.makeCuboidShape(WESTX1, DOWN1, NORTHZ1, EASTX2, UP2, SOUTHZ2);
    }

    public final boolean isConnected(IBlockReader worldIn, BlockPos pos, final ModelProperty<Boolean> property)
    {
        TETubeBase te = (TETubeBase) worldIn.getTileEntity(pos);
        if (te != null)
        {
            IModelData data = te.getModelData();
            return data.hasProperty(property) && data.getData(property);
        }
        return false;
    }
}
