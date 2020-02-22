package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockSignBase;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ItemPowerScrewDrive extends ItemBase
{

    public ItemPowerScrewDrive(Item.Properties properties)
    {
        super(properties.maxStackSize(1));
    }

    public static void playDrillSound(World world, BlockPos pos)
    {
        world.playSound(null, pos, SoundsRegistration.ITEM_DRILL.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState blockState = world.getBlockState(pos);
        if (context.getPlayer().isCrouching())
        {
            Block block = blockState.getBlock();
            PlayerEntity entity = context.getPlayer();
            if (block == BlocksRegistration.FLUIDPIPE.get()
                    || block == BlocksRegistration.ENERGYCABLELV.get()
                    || block == BlocksRegistration.ENERGYCABLEMV.get()
                    || block == BlocksRegistration.ENERGYCABLEHV.get())
            {
                ItemStack items = new ItemStack(block.asItem());
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative())
                {
                    entity.addItemStackToInventory(items);
                }
                world.removeBlock(pos, false);
                return ActionResultType.SUCCESS;
            } else if (block instanceof BlockSignBase)
            {
                ((BlockSignBase) block).changeSign(world, pos);
                playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            } else if (block instanceof BlockFloorPipe || block instanceof BlockFloorCable)
            {
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative())
                {
                    if (block == BlocksRegistration.FLOORPIPE.get())
                    {
                        entity.addItemStackToInventory(new ItemStack(BlocksRegistration.FLUIDPIPE_ITEM.get()));
                    } else if (block == BlocksRegistration.FLOORCABLEMV.get())
                    {
                        entity.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYCABLEMV_ITEM.get()));
                    } else if (block == BlocksRegistration.FLOORCABLELV.get())
                    {
                        entity.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYCABLELV_ITEM.get()));
                    } else if (block == BlocksRegistration.FLOORCABLEHV.get())
                    {
                        entity.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYCABLEHV_ITEM.get()));
                    } else if (block == BlocksRegistration.FLOORLAMP.get())
                    {
                        entity.addItemStackToInventory(new ItemStack(BlocksRegistration.FLUORESCENT_ITEM.get()));
                    }
                }
                world.setBlockState(pos, BlocksRegistration.INDFLOOR.get().getDefaultState());
                return ActionResultType.SUCCESS;
            } else if (world.getTileEntity(pos) instanceof TileEntityBatteryBank)
            {
                TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(pos);
                if (te != null) te.toggleFacing(context.getFace().getOpposite());
                world.notifyBlockUpdate(pos, blockState, blockState, 3);
                if (!world.isRemote) playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            }
        } else
        {
            Direction[] directions = blockState.getBlock().getValidRotations(blockState, world, pos);
            if (directions != null && directions.length > 0)
            {
                world.setBlockState(pos, blockState.rotate(Rotation.CLOCKWISE_90));
                playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        list.add(new StringTextComponent(I18n.format("item." + References.MODID + "." + getRegistryName().getPath() + ".des0")));
    }
}
