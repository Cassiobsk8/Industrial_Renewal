package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractFacingWithBase;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityEntityDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDetector extends BlockAbstractFacingWithBase
{
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockEntityDetector()
    {
        super(Block.Properties.create(Material.IRON), 8, 10);
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntityEntityDetector te = (TileEntityEntityDetector) worldIn.getTileEntity(pos);
        Direction facing = state.get(BASE);
        te.setBlockFacing(facing);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityEntityDetector createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityEntityDetector();
    }
}