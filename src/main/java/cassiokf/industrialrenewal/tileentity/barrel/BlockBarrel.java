package cassiokf.industrialrenewal.tileentity.barrel;

import cassiokf.industrialrenewal.blocks.BlockBasicContainer;
import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.network.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnBarrel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBarrel extends BlockBasicContainer<TileEntityBarrel>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool FRAME = PropertyBool.create("frame");
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);

    public BlockBarrel(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        TileEntityBarrel te = (TileEntityBarrel) worldIn.getTileEntity(pos);
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnBarrel(te));
        if (!worldIn.isRemote)
        {
            playerIn.sendMessage(new TextComponentString(te.GetChatQuantity()));
        }
        boolean save = FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing);
        if (save) {
            te.save();
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {

        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(FRAME, placer.isSneaking());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, FRAME);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityBarrel te = (TileEntityBarrel) worldIn.getTileEntity(pos);

        ItemStack itemst = SaveStackContainer(te);
        spawnAsEntity(worldIn, pos, itemst);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntityBarrel te = (TileEntityBarrel) world.getTileEntity(pos);
        return SaveStackContainer(te);
    }

    private ItemStack SaveStackContainer(TileEntityBarrel te)
    {
        ItemStack stack = new ItemStack(ModItems.barrel);
        if (te != null)
        {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) nbt = new NBTTagCompound();
            if (te.tank.getFluid() != null)
            {
                te.tank.writeToNBT(nbt);
                stack.setTagCompound(nbt);
            }
        }
        return stack;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(FRAME, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(FRAME))
        {
            i |= 4;
        }
        return i;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_AABB;
    }

    public Class<TileEntityBarrel> getTileEntityClass()
    {
        return TileEntityBarrel.class;
    }

    @Nullable
    @Override
    public TileEntityBarrel createTileEntity(World world, IBlockState state)
    {
        return new TileEntityBarrel();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBarrel();
    }
}
