package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTransformerHV extends Block3x3Top1Base<TileEntityTransformerHV>
{
    public static final PropertyInteger OUTPUT = PropertyInteger.create("output", 0, 2);

    public BlockTransformerHV(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, OUTPUT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        /*
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
         */
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntityTransformerHV te = (TileEntityTransformerHV) worldIn.getTileEntity(pos);
        int type;
        if (te == null || !te.isMaster()) type = 0;
        else type = (te.isOutPut) ? 2 : 1;
        return state.withProperty(OUTPUT, type);
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public Class<TileEntityTransformerHV> getTileEntityClass()
    {
        return TileEntityTransformerHV.class;
    }

    @Nullable
    @Override
    public TileEntityTransformerHV createTileEntity(World world, IBlockState state)
    {
        return new TileEntityTransformerHV();
    }
}
