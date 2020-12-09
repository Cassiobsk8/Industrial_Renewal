package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockSaveContent;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockBarrel extends BlockSaveContent
{
    public static final BooleanProperty FRAME = BooleanProperty.create("frame");

    public BlockBarrel()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(FRAME, context.getPlayer().isCrouching());
    }

    @Override
    public Item getItemToDrop()
    {
        return ItemsRegistration.BARREL.get();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, FRAME);
    }

    @Nullable
    @Override
    public TileEntityBarrel createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBarrel();
    }
}
