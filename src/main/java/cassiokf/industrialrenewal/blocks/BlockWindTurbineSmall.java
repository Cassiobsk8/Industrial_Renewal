package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.item.ItemWindBlade;
import cassiokf.industrialrenewal.tileentity.TileEntitySmallWindTurbine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
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

public class BlockWindTurbineSmall extends BlockAbstractHorizontalFacing
{
    public BlockWindTurbineSmall()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntitySmallWindTurbine tile = (TileEntitySmallWindTurbine) worldIn.getTileEntity(pos);
        if (tile == null) return ActionResultType.PASS;
        IItemHandler itemHandler = tile.getBladeHandler();
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemWindBlade || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemWindBlade && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isRemote)
                {
                    player.addItemStackToInventory(itemHandler.extractItem(0, 64, false));
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.produces")
                        + ": "
                        + (IRConfig.Main.maxEnergySWindTurbine.get().toString())
                        + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().isCrouching() ? context.getPlayer().getHorizontalFacing().getOpposite() : context.getPlayer().getHorizontalFacing());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntitySmallWindTurbine createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySmallWindTurbine();
    }
}
