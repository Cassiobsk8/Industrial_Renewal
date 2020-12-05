package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntityConnectedMultiblocks;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelFrame;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSolarPanelFrame extends BlockTileEntityConnectedMultiblocks<TileEntitySolarPanelFrame>
{
    public static final PropertyDirection FACING = BlockHorizontalFacing.FACING;

    public BlockSolarPanelFrame(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.requires")
                + ": "
                + ModBlocks.spanel.getLocalizedName());
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": "
                + ("Solar panel")
                + " * 2"
                + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntitySolarPanelFrame tile = (TileEntitySolarPanelFrame) world.getTileEntity(pos);
        if (tile == null) return false;
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (Block.getBlockFromItem(heldItem.getItem()) instanceof BlockSolarPanel && !tile.hasPanel())
            {
                if (!world.isRemote)
                {
                    tile.setPanelInv(true);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return true;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && tile.hasPanel())
            {
                if (!world.isRemote)
                {
                    tile.setPanelInv(false);
                    if (!player.isCreative()) player.addItemStackToInventory(tile.getPanel());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.isSneaking() ? placer.getHorizontalFacing().getOpposite() : placer.getHorizontalFacing());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Nullable
    @Override
    public TileEntitySolarPanelFrame createTileEntity(World world,  BlockState state)
    {
        return new TileEntitySolarPanelFrame();
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
