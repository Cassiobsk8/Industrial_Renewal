package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockFirstAidKit extends BlockHorizontalFacing
{
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.1875F, 0.1875F, 0.3125F, 0.8125F, 0.8125F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.1875F, 0.1875F, 0.6875F, 0.8125F, 0.8125F);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.1875F, 0.1875F, 0.6875F, 0.8125F, 0.8125F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.1875F, 0.1875F, 0.3125F, 0.8125F, 0.8125F, 0);

    public BlockFirstAidKit(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
    }

    public static EnumFacing getFaceDirection(IBlockState state)
    {
        if (state.getBlock() instanceof BlockFirstAidKit)
        {
            return state.getValue(FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing dir = state.getValue(FACING);
        switch (dir)
        {
            default:
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        if (!player.isSneaking())
        {
            ItemStack stack = itemInKit(world, pos);
            if (stack != null && player.shouldHeal() && !player.isPotionActive(MobEffects.REGENERATION))
            {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, IRConfig.MainConfig.Main.medKitEffectDuration, 1, false, false));
                stack.shrink(1);
            }
        }
        else
        {
            OpenGUI(world, pos, player);
        }
        return true;
    }

    private ItemStack itemInKit(World world, BlockPos pos)
    {
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(pos);
        if (te == null) return null;
        IItemHandler inventory = te.inventory;
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty())
            {
                return stack;
            }
        }
        return null;
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player)
    {
        player.openGui(IndustrialRenewal.instance, GUIHandler.FIRSTAIDKIT, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntityFirstAidKit te = (TileEntityFirstAidKit) world.getTileEntity(pos);
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (inventory != null)
        {
            Utils.dropInventoryItems(world, pos, inventory);
        }
        super.breakBlock(world, pos, state);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityFirstAidKit createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFirstAidKit();
    }
}