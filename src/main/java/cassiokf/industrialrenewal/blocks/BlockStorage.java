package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.tileentity.TEStorage;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nullable;
import java.util.List;

public class BlockStorage extends BlockMultiTankBase<TEStorage>
{
    public BlockStorage()
    {
        super(Block.Properties.create(Material.WOOD));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (hit.getFace() == Direction.UP) return ActionResultType.PASS;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TEStorage) return ((TEStorage) te).getMaster().getBottomTE().onActivated(player, handIn, hit);
        return ActionResultType.PASS;
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x3x2Centered(masterPos, facing);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.up(), state.with(MASTER, true));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, DOWN);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        boolean isMaster = stateIn.get(MASTER);
        return stateIn.with(DOWN, (isMaster ? isBot(worldIn, currentPos) : 0));
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockStorage;
    }

    @Nullable
    @Override
    public TEStorage createTileEntity(BlockState state, IBlockReader world)
    {
        return new TEStorage();
    }
}
