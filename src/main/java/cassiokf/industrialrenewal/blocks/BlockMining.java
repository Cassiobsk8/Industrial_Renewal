package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityMining;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

public class BlockMining extends BlockMultiBlockBase<TileEntityMining>
{
    public BlockMining()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMining) ((TileEntityMining) te).dropAllItems();
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        IRSoundHandler.stopTileSound(pos);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        TileEntityMining tile = (TileEntityMining) worldIn.getTileEntity(pos);
        if (tile == null) return ActionResultType.PASS;
        IItemHandler itemHandler = tile.getDrillHandler();
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemDrill || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemDrill && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getTag()), false);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty() && !tile.isRunning())
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
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.requires")
                + ":"));
        tooltip.add(new StringTextComponent(" -" + I18n.format("info.industrialrenewal.drill")));
        tooltip.add(new StringTextComponent(" -" + Blocks.WATER.getNameTextComponent().getFormattedText()
                + ": "
                + TileEntityMining.waterPerTick
                + " mB/t"));
        tooltip.add(new StringTextComponent(" -" + (TileEntityMining.energyPerTick)
                + " ~ "
                + TileEntityMining.deepEnergyPerTick
                + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public TileEntityMining createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityMining();
    }
}
