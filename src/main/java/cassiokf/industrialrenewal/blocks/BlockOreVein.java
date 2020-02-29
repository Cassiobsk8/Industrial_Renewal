package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.TileEntityOreVein;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockOreVein extends BlockTileEntity<TileEntityOreVein>
{

    public static final IntegerProperty QUANTITY = IntegerProperty.create("quantity", 0, 4);

    public BlockOreVein()
    {
        super(Block.Properties.create(Material.IRON));
        this.setDefaultState(this.getDefaultState().with(QUANTITY, 0));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntityOreVein te = (TileEntityOreVein) worldIn.getTileEntity(pos);
        if (te != null && player.getHeldItem(handIn).isEmpty())
        {
            if (!worldIn.isRemote) player.sendMessage(new StringTextComponent(te.getQuality().name()));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(QUANTITY);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        int stage = state.get(QUANTITY);
        if (stage < 4)
        {
            worldIn.setBlockState(pos, state.with(QUANTITY, stage + 1));
            //return;
        }
    }

    @Nullable
    @Override
    public TileEntityOreVein createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityOreVein();
    }
}
