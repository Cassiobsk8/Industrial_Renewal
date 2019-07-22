package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelFrame;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
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

public class BlockSolarPanelFrame extends BlockTileEntityConnectedMultiblocks<TileEntitySolarPanelFrame>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockSolarPanelFrame(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntitySolarPanelFrame tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getPanelHandler();
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return true;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    ItemStack panel = itemHandler.extractItem(0, 64, false);
                    if (!player.isCreative()) player.addItemStackToInventory(panel);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntitySolarPanelFrame te = (TileEntitySolarPanelFrame) worldIn.getTileEntity(pos);
        if (te != null) te.dropAllItems();
        super.breakBlock(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
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
    public Class<TileEntitySolarPanelFrame> getTileEntityClass()
    {
        return TileEntitySolarPanelFrame.class;
    }

    @Nullable
    @Override
    public TileEntitySolarPanelFrame createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySolarPanelFrame();
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

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
