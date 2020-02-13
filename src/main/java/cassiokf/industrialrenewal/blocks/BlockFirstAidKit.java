package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockFirstAidKit extends BlockTileEntity<TileEntityFirstAidKit>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.1875F, 0.1875F, 0.3125F, 0.8125F, 0.8125F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.1875F, 0.1875F, 0.6875F, 0.8125F, 0.8125F);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.1875F, 0.1875F, 0.6875F, 0.8125F, 0.8125F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.1875F, 0.1875F, 0.3125F, 0.8125F, 0.8125F, 0);


    public BlockFirstAidKit(Block.Properties property)
    {
        super(property);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            Direction dir = state.get(FACING);
            switch (dir) {
                default:
                case NORTH:
                    return NORTH_BLOCK_AABB;
                case SOUTH:
                    return SOUTH_BLOCK_AABB;
                case EAST:
                    return EAST_BLOCK_AABB;
                case WEST:
                    return WEST_BLOCK_AABB;
            }
        }
    */
    public static Direction getFaceDirection(BlockState state)
    {
        if (state.getBlock() instanceof BlockFirstAidKit)
        {
            return state.get(FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }
        if (!player.isCrouching())
        {
            ItemStack stack = itemInKit(worldIn, pos);
            if (stack != null && player.shouldHeal() && !player.isPotionActive(Effects.REGENERATION))
            {
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, IRConfig.Main.medKitEffectDuration.get(), 1, false, false));
                stack.shrink(1);
            }
        } else
        {
            OpenGUI(worldIn, pos, player);
        }
        return ActionResultType.SUCCESS;
    }

    private ItemStack itemInKit(World world, BlockPos pos)
    {
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(pos);
        if (te != null)
        {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
            if (inventory != null)
            {
                for (int slot = 0; slot < inventory.getSlots(); slot++)
                {
                    ItemStack stack = inventory.getStackInSlot(slot);
                    if (!stack.isEmpty())
                    {
                        return stack;
                    }
                }
            }
        }
        return null;
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        //player.openGui(IndustrialRenewal.instance, GUIHandler.FIRSTAIDKIT, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) worldIn.getTileEntity(pos);
        if (te != null)
        {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
            if (inventory != null)
            {
                for (int slot = 0; slot < inventory.getSlots(); slot++)
                {
                    ItemStack stack = inventory.getStackInSlot(slot);
                    if (!stack.isEmpty())
                    {
                        ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                        worldIn.addEntity(item);
                    }
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Nullable
    @Override
    public TileEntityFirstAidKit createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFirstAidKit();
    }
}