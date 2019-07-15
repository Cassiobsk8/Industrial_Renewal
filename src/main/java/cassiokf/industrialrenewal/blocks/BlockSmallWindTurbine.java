package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.item.ItemWindBlade;
import cassiokf.industrialrenewal.tileentity.TileEntitySmallWindTurbine;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockSmallWindTurbine extends BlockTileEntity<TileEntitySmallWindTurbine>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockSmallWindTurbine(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntitySmallWindTurbine tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getBladeHandler();
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemWindBlade || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemWindBlade && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    heldItem.shrink(1);
                }
                return true;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    player.addItemStackToInventory(itemHandler.extractItem(0, 64, false));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntitySmallWindTurbine te = (TileEntitySmallWindTurbine) worldIn.getTileEntity(pos);
        if (te != null) te.dropAllItems();
        super.breakBlock(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.isSneaking() ? placer.getHorizontalFacing().getOpposite() : placer.getHorizontalFacing());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Class<TileEntitySmallWindTurbine> getTileEntityClass()
    {
        return TileEntitySmallWindTurbine.class;
    }

    @Nullable
    @Override
    public TileEntitySmallWindTurbine createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySmallWindTurbine();
    }
}
