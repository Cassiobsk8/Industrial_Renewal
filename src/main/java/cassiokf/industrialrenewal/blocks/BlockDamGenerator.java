package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.tileentity.TileEntityDamGenerator;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDamGenerator extends BlockMultiBlockBase<TileEntityDamGenerator>
{
    public BlockDamGenerator(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": "
                + (TileEntityDamGenerator.maxGeneration)
                + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(masterPos);
    }

    @Nullable
    @Override
    public TileEntityDamGenerator createTileEntity(World world,  BlockState state)
    {
        return new TileEntityDamGenerator();
    }
}
