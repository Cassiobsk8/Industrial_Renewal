package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockSignBase;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemPowerScrewDrive extends ItemBase {


    public ItemPowerScrewDrive(Properties properties)
    {
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (context.getPlayer().isSneaking())
        {
            PlayerEntity player = context.getPlayer();
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (block == BlocksRegistration.FLUIDPIPE.get()
                    || block == BlocksRegistration.ENERGYCABLEGAUGEMV.get()
                    || block == BlocksRegistration.ENERGYCABLEGAUGELV.get()
                    || block == BlocksRegistration.ENERGYCABLEGAUGEHV.get())
            {
                ItemStack items = new ItemStack(world.getBlockState(pos).getBlock().asItem(), 1);
                playDrillSound(world, pos);
                if (!world.isRemote && !player.isCreative())
                {
                    player.inventory.addItemStackToInventory(items);
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
                if (!world.isRemote && !player.isCreative())
                {
                    if (block == BlocksRegistration.FLOORPIPE.get())
                    {
                        player.inventory.addItemStackToInventory(new ItemStack(BlocksRegistration.FLUIDPIPE_ITEM.get(), 1));
                    }
                    else if (block == BlocksRegistration.FLOORCABLEMV.get())
                    {
                        player.inventory.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYCABLEMV_ITEM.get(), 1));
                    }
                    else if (block == BlocksRegistration.FLOORCABLELV.get())
                    {
                        player.inventory.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYCABLELV_ITEM.get(), 1));
                    }
                    else if (block == BlocksRegistration.FLOORCABLEHV.get())
                    {
                        player.inventory.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYCABLEHV_ITEM.get(), 1));
                    }
                    else if (block == BlocksRegistration.FLOORLAMP.get())
                    {
                        player.inventory.addItemStackToInventory(new ItemStack(BlocksRegistration.FLUORESCENT_ITEM.get(), 1));
                    }
                }
                world.setBlockState(new BlockPos(x, y, z), BlocksRegistration.INDFLOOR.get().getDefaultState(), 3);
                return ActionResultType.SUCCESS;
            } else if (world.getTileEntity(pos) instanceof TileEntityBatteryBank)
            {
                TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(pos);
                if (te != null) te.toggleFacing(context.getFace().getOpposite());
                world.notifyBlockUpdate(pos, blockState, blockState, 3);
                if (!world.isRemote) playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            }
            //TODO adicionar para remover os gates
        } else
        {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock().rotate(state, world, pos, Rotation.CLOCKWISE_90) != state)
            {
                playDrillSound(world, pos);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(new StringTextComponent(I18n.format("item." + References.MODID + "." + name + ".des0")));
    }

    public static void playDrillSound(World world, BlockPos pos)
    {
        world.playSound(null, pos, SoundsRegistration.ITEM_DRILL, SoundCategory.BLOCKS, 1.0F * IRConfig.Sounds.masterVolumeMult.get(), 1.0F);
    }
}
