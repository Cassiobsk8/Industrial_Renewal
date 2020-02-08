package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
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
        world.playSound(null, pos, IRSoundRegister.ITEM_DRILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState blockState = world.getBlockState(pos);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (context.getPlayer().isCrouching())
        {/*
            Block block = blockState.getBlock();
            PlayerEntity entity = context.getPlayer();
            if (block == ModBlocks.fluidPipe || block == ModBlocks.energyCableMV)
            {
                ItemStack items = new ItemStack(world.getBlockState(pos).getBlock().asItem(), 1);
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative())
                {
                    entity.inventory.addItemStackToInventory(items);
                }
                world.removeBlock(pos, false);
                return ActionResultType.SUCCESS;
            } else if (block instanceof BlockSignBase)
            {
                ((BlockSignBase) world.getBlockState(pos).getBlock()).changeSign(world, pos);
                playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            } else if (block instanceof BlockFloorPipe || block instanceof BlockFloorCable)
            {
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative())
                {
                    if (block == ModBlocks.floorPipe)
                    {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.fluidPipe), 1));
                    } else if (block == ModBlocks.floorCableMV)
                    {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.energyCableMV), 1));
                    } else if (block == ModBlocks.floorCableLV)
                    {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.energyCableLV), 1));
                    } else if (block == ModBlocks.floorCableHV)
                    {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.energyCableHV), 1));
                    } else if (block == ModBlocks.floorLamp)
                    {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.fluorescent), 1));
                    }
                }
                world.setBlockState(new BlockPos(x, y, z), ModBlocks.blockIndFloor.getDefaultState(), 3);
                return ActionResultType.SUCCESS;
            } else if (world.getTileEntity(pos) instanceof TileEntityBatteryBank)
            {
                TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(pos);
                if (te != null) te.toggleFacing(side.getOpposite());
                world.notifyBlockUpdate(pos, blockState, blockState, 3);
                if (!world.isRemote) playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            }
            */
        } else
        {
            if (blockState.getBlock().getValidRotations(blockState, world, pos) != null)
            {
                blockState.getBlock().rotate(blockState, world, pos, Rotation.CLOCKWISE_90);
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
