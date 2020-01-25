package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockSignBase;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPowerScrewDrive extends ItemBase {

    public ItemPowerScrewDrive(String name, CreativeTabs tab) {
        super(name, tab);
        maxStackSize = 1;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer entity, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (entity.isSneaking())
        {
            IBlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (block == ModBlocks.fluidPipe || block == ModBlocks.energyCableMV)
            {
                ItemStack items = new ItemStack(ItemBlock.getItemFromBlock(world.getBlockState(pos).getBlock()), 1);
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative())
                {
                    entity.inventory.addItemStackToInventory(items);
                }
                world.setBlockToAir(pos);
                return EnumActionResult.SUCCESS;
            } else if (block instanceof BlockSignBase)
            {
                ((BlockSignBase) world.getBlockState(pos).getBlock()).changeSign(world, pos);
                playDrillSound(world, pos);
                return EnumActionResult.SUCCESS;
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
                return EnumActionResult.SUCCESS;
            } else if (world.getTileEntity(pos) instanceof TileEntityBatteryBank)
            {
                TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(pos);
                if (te != null) te.toggleFacing(side.getOpposite());
                world.notifyBlockUpdate(pos, blockState, blockState, 3);
                if (!world.isRemote) playDrillSound(world, pos);
                return EnumActionResult.SUCCESS;
            }
            //TODO adicionar para remover os gates
        } else
        {
            if (world.getBlockState(pos).getBlock().rotateBlock(world, pos, EnumFacing.UP))
            {
                playDrillSound(world, pos);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        list.add(I18n.format("item." + References.MODID + "." + name + ".des0"));
    }

    public static void playDrillSound(World world, BlockPos pos)
    {
        world.playSound(null, pos, IRSoundRegister.ITEM_DRILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
