package cassiokf.industrialrenewal.blocks.machines;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.machines.TEIndustrialBatteryBank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockIndustrialBatteryBank extends BlockMultiTankBase<TEIndustrialBatteryBank>
{
    public BlockIndustrialBatteryBank(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("info.industrialrenewal.fluid_tank.info"));
        tooltip.add(I18n.format("info.industrialrenewal.requires")
                + ": 24 x "
                + I18n.format("item.industrialrenewal.battery_lithium.name"));
        tooltip.add(I18n.format("info.industrialrenewal.capacity")
                + ": "
                + Utils.formatEnergyString(TEIndustrialBatteryBank.capacity * 24));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,  BlockState state, PlayerEntity playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (stack.getItem().equals(ModItems.battery_lithium))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TEIndustrialBatteryBank)
                return ((TEIndustrialBatteryBank) te).getMaster().placeBattery(playerIn, stack);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockIndustrialBatteryBank;
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TEIndustrialBatteryBank createTileEntity(World world,  BlockState state)
    {
        return new TEIndustrialBatteryBank();
    }
}
