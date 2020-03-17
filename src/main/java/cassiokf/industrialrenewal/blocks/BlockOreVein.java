package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityOreVein;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockOreVein extends BlockBase
{

    public static final PropertyInteger QUANTITY = PropertyInteger.create("quantity", 0, 4);

    public BlockOreVein(String name, CreativeTabs tab)
    {
        super(Material.ROCK, name, tab);
        this.setHardness(8f);
        this.setDefaultState(this.getDefaultState().withProperty(QUANTITY, 0));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityOreVein te = (TileEntityOreVein) worldIn.getTileEntity(pos);
        if (te != null && playerIn.getHeldItem(hand).isEmpty())
        {
            if (!worldIn.isRemote) playerIn.sendMessage(new TextComponentString(te.getQuality().name()));
            return true;
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, QUANTITY);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        int stage = state.getActualState(worldIn, pos).getValue(QUANTITY);
        if (stage < 4)
        {
            worldIn.setBlockState(pos, state.withProperty(QUANTITY, stage + 1));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModItems.hematiteChunk;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return 1 + fortune;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        return MathHelper.getInt(rand, 3, 7);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(QUANTITY, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(QUANTITY);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityOreVein createTileEntity(World world, IBlockState state)
    {
        return new TileEntityOreVein();
    }
}
