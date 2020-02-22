package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityLoaderRail;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockLoaderRail extends BlockRailFacing
{
    public BlockLoaderRail()
    {
        super(Block.Properties.create(Material.IRON));
        setDefaultState(getDefaultState()
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(FACING, Direction.NORTH));
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        TileEntityLoaderRail te = (TileEntityLoaderRail) world.getTileEntity(pos);
        if (te != null) te.onMinecartPass(cart);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        String str = I18n.format("tile.industrialrenewal.loader_rail.info") + " " + BlocksRegistration.CARGOLOADER.get().getNameTextComponent().getFormattedText();
        tooltip.add(new StringTextComponent(str));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityLoaderRail createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityLoaderRail();
    }
}