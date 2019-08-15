package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityLoaderRail;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLoaderRail extends BlockRailFacing
{
    public BlockLoaderRail(String name, CreativeTabs tab) {
        super(name, tab);
        setSoundType(SoundType.METAL);
        setDefaultState(blockState.getBaseState()
                .withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH)
                .withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        TileEntityLoaderRail te = (TileEntityLoaderRail) world.getTileEntity(pos);
        if (te != null) te.onMinecartPass(cart);
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumRailDirection shape = EnumRailDirection.byMetadata((meta >> 1) & 1);
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta >> 2);

        return getDefaultState()
                .withProperty(SHAPE, shape)
                .withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumRailDirection shape = state.getValue(SHAPE);
        EnumFacing facing = state.getValue(FACING);
        int meta = 0;
        meta |= shape.getMetadata() << 1;
        meta |= facing.getHorizontalIndex() << 2;
        return meta;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE, FACING);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        String str = I18n.format("tile.industrialrenewal.loader_rail.info") + " " + ModBlocks.cargoLoader.getLocalizedName();
        tooltip.add(str);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public Class<TileEntityLoaderRail> getTileEntityClass() {
        return TileEntityLoaderRail.class;
    }

    @Nullable
    @Override
    public TileEntityLoaderRail createTileEntity(World world, IBlockState state) {
        return new TileEntityLoaderRail();
    }
}