package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityMining;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockMining extends BlockMultiBlockBase<TileEntityMining>
{
    public BlockMining(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos,  BlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMining) ((TileEntityMining) te).dropAllItems();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityMining tile = (TileEntityMining) world.getTileEntity(pos);
        if (tile == null) return false;
        IItemHandler itemHandler = tile.getDrillHandler();
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemDrill || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemDrill && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getItem().getDamage(heldItem)), false);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return true;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty() && !tile.isRunning())
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
        tooltip.add(I18n.format("info.industrialrenewal.requires")
                + ":");
        tooltip.add(" -" + I18n.format("info.industrialrenewal.drill"));
        tooltip.add(" -" + Blocks.WATER.getLocalizedName()
                + ": "
                + TileEntityMining.waterPerTick
                + " mB/t");
        tooltip.add(" -" + (TileEntityMining.energyPerTick)
                + " ~ "
                + TileEntityMining.deepEnergyPerTick
                + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER);
    }

    @Nullable
    @Override
    public TileEntityMining createTileEntity(World world,  BlockState state)
    {
        return new TileEntityMining();
    }
}
