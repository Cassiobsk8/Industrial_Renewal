package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.FluidInit;
import cassiokf.industrialrenewal.item.ItemFireBox;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamBoiler;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSteamBoiler extends BlockMultiBlockBase<TileEntitySteamBoiler>
{
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);

    public BlockSteamBoiler(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.requires")
                + ":");
        tooltip.add(" -" + I18n.format("info.industrialrenewal.firebox"));
        tooltip.add(" -" + Blocks.WATER.getLocalizedName()
                + ": "
                + IRConfig.MainConfig.Main.steamBoilerWaterPerTick
                + " mB/t");
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + " "
                + FluidInit.STEAM.getName()
                + ": "
                + (IRConfig.MainConfig.Main.steamBoilerWaterPerTick * IRConfig.MainConfig.Main.steamBoilerConversionFactor)
                + " mB/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te != null && te.isMaster() && te.getType() == 1 && te.getFuelFill() > 0 && rand.nextInt(24) == 0)
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.3F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te != null) te.dropAllItems();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntitySteamBoiler tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getFireBoxHandler();
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemFireBox || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemFireBox && itemHandler.getStackInSlot(0).isEmpty())
            {
                int type = ((ItemFireBox) heldItem.getItem()).type;
                itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                tile.setType(type);
                if (!world.isRemote && !player.isCreative()) heldItem.shrink(1);
                return true;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                ItemStack stack = itemHandler.extractItem(0, 64, false);
                if (!world.isRemote && !player.isCreative()) player.addItemStackToInventory(stack);
                tile.setType(0);
                return true;
            }
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, TYPE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te == null || !state.getValue(MASTER)) return state.withProperty(TYPE, 0);
        return state.withProperty(TYPE, te.getType());
    }

    @Nullable
    @Override
    public TileEntitySteamBoiler createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySteamBoiler();
    }
}
