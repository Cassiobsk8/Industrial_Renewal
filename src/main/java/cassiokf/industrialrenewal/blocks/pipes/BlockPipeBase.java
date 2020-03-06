package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.abstracts.BlockConnectedMultiblocks;
import cassiokf.industrialrenewal.tileentity.abstracts.TETubeBase;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;

public abstract class BlockPipeBase<TE extends TileEntityMultiBlocksTube> extends BlockConnectedMultiblocks<TE>
{

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static double UP2 = 16;
    private static double DOWN1 = 0;

    public float nodeWidth;
    public float nodeHeight;

    public BlockPipeBase(Block.Properties property, float nodeWidth, float nodeHeight)
    {
        super(property);
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
    }


    public boolean isMaster(IBlockReader world, BlockPos pos)
    {
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) world.getTileEntity(pos);
        return te != null && te.isMaster();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (isConnected(worldIn, pos, TETubeBase.NORTH) || isConnected(worldIn, pos, TETubeBase.CNORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, TETubeBase.SOUTH) || isConnected(worldIn, pos, TETubeBase.CSOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = 8 + (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, TETubeBase.WEST) || isConnected(worldIn, pos, TETubeBase.CWEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, TETubeBase.EAST) || isConnected(worldIn, pos, TETubeBase.CEAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = 8 + (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, TETubeBase.DOWN) || isConnected(worldIn, pos, TETubeBase.CDOWN))
        {
            DOWN1 = 0;
        } else
        {
            DOWN1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, TETubeBase.UP) || isConnected(worldIn, pos, TETubeBase.CUP))
        {
            UP2 = 16;
        } else
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
