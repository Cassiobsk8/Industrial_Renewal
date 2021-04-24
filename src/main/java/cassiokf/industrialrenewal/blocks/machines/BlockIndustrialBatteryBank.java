package cassiokf.industrialrenewal.blocks.machines;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.tileentity.machines.TEIndustrialBatteryBank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockIndustrialBatteryBank extends BlockMultiTankBase<TEIndustrialBatteryBank>
{
    public BlockIndustrialBatteryBank()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.fluid_tank.info")));
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.requires")
                + ": 24 x "
                + I18n.format("item.industrialrenewal.battery_lithium.name")));
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.capacity")
                + ": "
                + Utils.formatEnergyString(TEIndustrialBatteryBank.capacity * 24)));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack stack = player.getHeldItem(handIn);
        if (stack.getItem().equals(ItemsRegistration.BATTERY_LITHIUM.get()))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TEIndustrialBatteryBank)
                return ((TEIndustrialBatteryBank) te).getMaster().placeBattery(player, stack)
                        ? ActionResultType.SUCCESS
                        : ActionResultType.PASS;
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockIndustrialBatteryBank;
    }

    @Nullable
    @Override
    public TEIndustrialBatteryBank createTileEntity(BlockState state, IBlockReader world)
    {
        return new TEIndustrialBatteryBank();
    }
}
