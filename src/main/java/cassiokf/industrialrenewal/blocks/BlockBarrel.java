package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityBarrel;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBarrel extends BlockHorizontalFacing
{
    public static final PropertyBool FRAME = PropertyBool.create("frame");

    public BlockBarrel(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityBarrel && !FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing))
                playerIn.sendMessage(new TextComponentString(((TileEntityBarrel) te).GetChatQuantity()));
        }
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {

        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(FRAME, placer.isSneaking());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, FRAME);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityBarrel)
        {
            ItemStack itemst = SaveStackContainer((TileEntityBarrel) te);
            spawnAsEntity(worldIn, pos, itemst);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        return te instanceof TileEntityBarrel ? (int) (Utils.normalize(((TileEntityBarrel) te).tank.getFluidAmount(), 0, ((TileEntityBarrel) te).tank.getCapacity()) * 15) : 0;
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

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(FRAME, (meta & 4) > 0);
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityBarrel createTileEntity(World world, IBlockState state)
    {
        return new TileEntityBarrel();
    }
}
