package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockFirstAidKit extends BlockAbstractHorizontalFacing
{
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 3, 3, 5, 13, 13);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(11, 3, 3, 16, 13, 13);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(3, 3, 11, 13, 13, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(3, 3, 0, 13, 13, 5);


    public BlockFirstAidKit()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        Direction dir = state.get(FACING);
        switch (dir)
        {
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
        if (state.getBlock() == newState.getBlock()) return;
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) worldIn.getTileEntity(pos);
        if (te != null)
        {

            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
            Utils.dropInventoryItems(worldIn, pos, inventory);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
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