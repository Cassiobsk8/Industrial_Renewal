package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityBunkBed;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BedPart.FOOT;

public class BlockBunkBed extends BedBlock
{
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public BlockBunkBed()
    {
        super(DyeColor.RED, Block.Properties.create(Material.IRON).hardnessAndResistance(2F, 10F));
    }
/* need to found the correct function
    @Nullable
    @Override
    public BlockPos getBedSpawnPosition(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityPlayer player)
    {
        Block down = world.getBlockState(pos.down()).getBlock();
        if (down instanceof BlockBunkBed)
        {
            state = world.getBlockState(pos.down());
            return down.getBedSpawnPosition(state, world, pos.down(), player);
        }
        return super.getBedSpawnPosition(state, world, pos, player);
    }
*/
    @Override
    public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }


    private boolean isBunkBed(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockBunkBed;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        if (!player.isCreative())
            Utils.spawnItemStack(worldIn, pos, new ItemStack(BlocksRegistration.BUNKBED_ITEM.get()));
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        boolean top = stateIn.get(PART) == FOOT && worldIn.getBlockState(currentPos.down()).getBlock() instanceof BlockBunkBed;
        return super.updatePostPlacement(stateIn.with(TOP, top), facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(HORIZONTAL_FACING, PART, OCCUPIED, TOP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        boolean top = context.getWorld().getBlockState(context.getPos().down()).getBlock() instanceof BlockBunkBed;
        return super.getStateForPlacement(context).with(TOP, top);
    }

    @Nullable
    @Override
    public TileEntityBunkBed createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBunkBed();
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new TileEntityBunkBed();
    }
}
