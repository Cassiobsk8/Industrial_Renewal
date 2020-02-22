package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemFireBox;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamBoiler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSteamBoiler extends Block3x3x3Base<TileEntitySteamBoiler>
{
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

    public BlockSteamBoiler()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.requires")
                        + ":"));
        tooltip.add(new StringTextComponent(" -" + I18n.format("info.industrialrenewal.firebox")));
        tooltip.add(new StringTextComponent(
                " -" + Blocks.WATER.getNameTextComponent().getFormattedText()
                        + ": "
                        + IRConfig.Main.steamBoilerWaterPerTick.get().toString()
                        + " mB/t"));
        int mult = IRConfig.Main.steamBoilerWaterPerTick.get() * IRConfig.Main.steamBoilerConversionFactor.get();
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.produces")
                        + " "
                        + "Steam"
                        + ": "
                        + mult
                        + " mB/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te != null && te.isMaster() && te.getIntType() == 1 && te.getFuelFill() > 0 && rand.nextInt(24) == 0)
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.3F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te != null) te.dropAllItems();
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntitySteamBoiler tile = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        IItemHandler itemHandler = tile.getFireBoxHandler();
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemFireBox || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemFireBox && itemHandler.getStackInSlot(0).isEmpty())
            {
                int type = ((ItemFireBox) heldItem.getItem()).type;
                itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                tile.setType(type);
                if (!worldIn.isRemote && !player.isCreative()) heldItem.shrink(1);
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                ItemStack stack = itemHandler.extractItem(0, 64, false);
                if (!worldIn.isRemote && !player.isCreative()) player.addItemStackToInventory(stack);
                tile.setType(0);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, TYPE);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(currentPos);
        if (te == null || !stateIn.get(MASTER)) return stateIn.with(TYPE, 0);
        return stateIn.with(TYPE, te.getIntType());
    }

    @Nullable
    @Override
    public TileEntitySteamBoiler createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySteamBoiler();
    }
}
