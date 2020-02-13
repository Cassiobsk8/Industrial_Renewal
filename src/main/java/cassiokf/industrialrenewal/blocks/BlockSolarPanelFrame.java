package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelFrame;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSolarPanelFrame extends BlockTileEntityConnectedMultiblocks<TileEntitySolarPanelFrame>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public BlockSolarPanelFrame(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.requires")
                        + ": "
                        + ModBlocks.spanel.getNameTextComponent().getFormattedText()));
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.produces")
                        + ": "
                        + ("Solar panel")
                        + " * 2"
                        + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntitySolarPanelFrame tile = getTileEntity(worldIn, pos);
        IItemHandler itemHandler = tile.getPanelHandler();
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isRemote)
                {
                    ItemStack panel = itemHandler.extractItem(0, 64, false);
                    if (!player.isCreative()) player.addItemStackToInventory(panel);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntitySolarPanelFrame te = (TileEntitySolarPanelFrame) worldIn.getTileEntity(pos);
        if (te != null) te.dropAllItems();
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().isCrouching() ? context.getPlayer().getHorizontalFacing().getOpposite() : context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntitySolarPanelFrame createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySolarPanelFrame();
    }
}
