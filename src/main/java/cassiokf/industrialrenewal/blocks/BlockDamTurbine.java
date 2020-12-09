package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.tileentity.TileEntityDamTurbine;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDamTurbine extends BlockMultiBlockBase<TileEntityDamTurbine>
{
    public BlockDamTurbine()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(masterPos);
    }

    @Nullable
    @Override
    public TileEntityDamTurbine createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityDamTurbine();
    }
}
