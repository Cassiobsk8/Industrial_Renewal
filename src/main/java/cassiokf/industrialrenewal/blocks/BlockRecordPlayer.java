package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockRecordPlayer extends BlockTileEntity<TileEntityRecordPlayer>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty DOWNNOTEBLOCK = BooleanProperty.create("downnoteblock");
    public static final BooleanProperty DISK0 = BooleanProperty.create("disc0");
    public static final BooleanProperty DISK1 = BooleanProperty.create("disc1");
    public static final BooleanProperty DISK2 = BooleanProperty.create("disc2");
    public static final BooleanProperty DISK3 = BooleanProperty.create("disc3");

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.875D, 1.0D);

    public BlockRecordPlayer(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(DISK0, false).with(DISK1, false)
                .with(DISK2, false).with(DISK3, false).with(DOWNNOTEBLOCK, false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (!worldIn.isRemote)
        {
            OpenGUI(worldIn, pos, player);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntityRecordPlayer te = (TileEntityRecordPlayer) worldIn.getTileEntity(pos);
        if (te != null)
        {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
            if (inventory != null)
            {
                Utils.dropInventoryItems(worldIn, pos, inventory);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        //player.openGui(IndustrialRenewal.instance, GUIHandler.RECORDPLAYER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean isDownBlockaNoteBlock(IBlockReader world, BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() instanceof NoteBlock;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntityRecordPlayer te = (TileEntityRecordPlayer) worldIn.getTileEntity(currentPos);
        stateIn = stateIn.with(DOWNNOTEBLOCK, isDownBlockaNoteBlock(worldIn, currentPos))
                .with(DISK0, te.hasDiskInSlot(3))
                .with(DISK1, te.hasDiskInSlot(2))
                .with(DISK2, te.hasDiskInSlot(1))
                .with(DISK3, te.hasDiskInSlot(0));
        return stateIn;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(DOWNNOTEBLOCK, isDownBlockaNoteBlock(context.getWorld(), context.getPos()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, DOWNNOTEBLOCK, DISK0, DISK1, DISK2, DISK3);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            return BASE_AABB;
        }
    */
    @Nullable
    @Override
    public TileEntityRecordPlayer createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityRecordPlayer();
    }
}
