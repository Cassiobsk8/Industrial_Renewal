package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacingMultiblocks;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelFrame;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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

public class BlockSolarPanelFrame extends BlockTEHorizontalFacingMultiblocks<TileEntitySolarPanelFrame>
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockSolarPanelFrame()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.requires")
                        + ": "
                        + BlocksRegistration.SPANEL.get().getNameTextComponent().getFormattedText()));
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
        TileEntitySolarPanelFrame tile = (TileEntitySolarPanelFrame) worldIn.getTileEntity(pos);
        if (tile == null) return ActionResultType.PASS;
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel && !tile.hasPanel())
            {
                if (!worldIn.isRemote)
                {
                    tile.setPanelInv(true);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && tile.hasPanel())
            {
                if (!worldIn.isRemote)
                {
                    tile.setPanelInv(false);
                    if (!player.isCreative()) player.addItemStackToInventory(tile.getPanel());
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
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
