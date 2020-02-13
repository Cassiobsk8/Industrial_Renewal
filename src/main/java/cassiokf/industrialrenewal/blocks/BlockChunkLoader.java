package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityChunkLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockChunkLoader extends BlockTileEntity<TileEntityChunkLoader>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    public BlockChunkLoader(Block.Properties properties)
    {
        super(properties);
    }

    private static void activateChunkLoader(World worldIn, BlockPos pos, PlayerEntity placer)
    {
        //final ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IndustrialRenewal.instance, placer.getName(), worldIn, ForgeChunkManager.Type.NORMAL);

        //if (ticket == null)
        //{
        //    placer.sendStatusMessage(new StringTextComponent("Could not request any more chunk loading tickets"), true);
        //    //Player has requested too many tickets. Forge will log an issue here.
        //    return;
        //}

        //final IBlockState blockState = worldIn.getBlockState(pos);
        //if (blockState.getBlock() != BlockLibrary.weirding_gadget) return;

        //final CompoundNBT modData = ticket.getModData();
        //modData.setTag("blockPosition", NBTUtil.createPosTag(pos));
        //modData.setInteger("size", IRConfig.MainConfig.Main.chunkLoaderWidth);

        //ChunkManagerCallback.activateTicket(worldIn, ticket);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            worldIn.setBlockState(pos.up(), state.with(MASTER, false));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        if (!(placer instanceof PlayerEntity) || !state.get(MASTER))
        {
            return;
        }
        activateChunkLoader(worldIn, pos, (PlayerEntity) placer);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (!state.get(MASTER)) return ActionResultType.PASS;

        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }

        final TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
        if (tileEntity == null) return ActionResultType.PASS;

        boolean success = false;
        //final Iterable<TileEntityChunkLoader> chainedGadgets = ChunkManagerCallback.getChainedGadgets(tileEntity);

        if ((tileEntity.isExpired() || !tileEntity.hasTicket(player)))
        {
            //for (final TileEntityChunkLoader chainedGadget : chainedGadgets)
            //{
            //    activateChunkLoader(worldIn, chainedGadget.getPos(), player);
            //}

            success = true;
        }

        return success ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            if (IsLoader(worldIn, pos.up())) worldIn.removeBlock(pos.up(), false);

            final TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
            if (tileEntity == null) return;
            tileEntity.expireAllTickets();
        } else
        {
            if (IsLoader(worldIn, pos.down()))
                worldIn.removeBlock(pos.down(), false);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private boolean IsLoader(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockChunkLoader;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up()).getMaterial().isReplaceable();
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntityChunkLoader te = (TileEntityChunkLoader) worldIn.getTileEntity(currentPos);
        return stateIn.with(WORKING, stateIn.get(MASTER) && te != null && te.isActive());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, WORKING);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(MASTER, true);
    }

    @Override
    @Deprecated
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        final TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
        if (tileEntity == null) return false;

        tileEntity.receiveClientEvent(id, param);
        return true;
    }

    @Nullable
    @Override
    public TileEntityChunkLoader createTileEntity(BlockState state, IBlockReader world)
    {
        return null;
    }
}
