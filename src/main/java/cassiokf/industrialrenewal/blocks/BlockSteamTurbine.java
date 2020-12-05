package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.FluidInit;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamTurbine;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSteamTurbine extends BlockMultiBlockBase<TileEntitySteamTurbine>
{
    public BlockSteamTurbine(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.requires")
                + ": "
                + FluidInit.STEAM.getName()
                + " "
                + (IRConfig.MainConfig.Main.steamTurbineSteamPerTick)
                + " mB/t");
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": "
                + (IRConfig.MainConfig.Main.steamTurbineEnergyPerTick)
                + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Nullable
    @Override
    public TileEntitySteamTurbine createTileEntity(World world,  BlockState state)
    {
        return new TileEntitySteamTurbine();
    }
}
