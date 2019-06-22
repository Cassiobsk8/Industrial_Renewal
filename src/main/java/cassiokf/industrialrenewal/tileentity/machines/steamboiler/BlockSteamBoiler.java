package cassiokf.industrialrenewal.tileentity.machines.steamboiler;

import cassiokf.industrialrenewal.item.ItemFireBox;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.Block3x3x3Base;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
import java.util.Random;

public class BlockSteamBoiler extends Block3x3x3Base<TileEntitySteamBoiler>
{
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);

    public BlockSteamBoiler(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te != null && te.isMaster() && te.getType() == 1 && te.getFuelFill() > 0 && rand.nextInt(24) == 0)
        {
            worldIn.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.3F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
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
                if (!world.isRemote)
                {
                    int type = ((ItemFireBox) heldItem.getItem()).type;
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    heldItem.shrink(1);
                    tile.setType(type);
                }
                return true;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isRemote)
                {
                    player.addItemStackToInventory(itemHandler.extractItem(0, 64, false));
                    tile.setType(0);
                }
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

    @Override
    public Class<TileEntitySteamBoiler> getTileEntityClass()
    {
        return TileEntitySteamBoiler.class;
    }

    @Nullable
    @Override
    public TileEntitySteamBoiler createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySteamBoiler();
    }
}
