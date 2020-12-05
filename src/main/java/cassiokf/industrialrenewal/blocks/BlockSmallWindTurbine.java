package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.item.ItemWindBlade;
import cassiokf.industrialrenewal.tileentity.TileEntitySmallWindTurbine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSmallWindTurbine extends BlockHorizontalFacing
{
    public BlockSmallWindTurbine(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntitySmallWindTurbine tile = (TileEntitySmallWindTurbine) world.getTileEntity(pos);
        IItemHandler itemHandler = tile.getBladeHandler();
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemWindBlade || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemWindBlade && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getItem().getDamage(heldItem)), false);
                    if (!player.isCreative()) heldItem.shrink(1);
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
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": "
                + (IRConfig.MainConfig.Main.maxEnergySWindTurbine)
                + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos,  BlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntitySmallWindTurbine) ((TileEntitySmallWindTurbine) te).dropAllItems();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.isSneaking() ? placer.getHorizontalFacing().getOpposite() : placer.getHorizontalFacing());
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntitySmallWindTurbine createTileEntity(World world,  BlockState state)
    {
        return new TileEntitySmallWindTurbine();
    }
}
