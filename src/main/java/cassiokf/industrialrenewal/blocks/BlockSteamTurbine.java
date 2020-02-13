package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamTurbine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSteamTurbine extends Block3x3x3Base<TileEntitySteamTurbine>
{
    public BlockSteamTurbine(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.requires")
                        + ": "
                        + ModBlocks.steamBlock.getNameTextComponent().getFormattedText()
                        + " "
                        + (IRConfig.Main.steamTurbineSteamPerTick.get().toString())
                        + " mB/t"));
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.produces")
                        + ": "
                        + (IRConfig.Main.steamTurbineEnergyPerTick.get().toString())
                        + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public TileEntitySteamTurbine createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySteamTurbine();
    }
}
