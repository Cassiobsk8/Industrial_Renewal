package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.TileEntityBarrel;
import cassiokf.industrialrenewal.tileentity.TileEntityPortableGenerator;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySaveContent;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import java.util.List;
import java.util.Random;

public abstract class BlockSaveContent extends BlockHorizontalFacing
{
    public BlockSaveContent(String name, CreativeTabs tab, Material material)
    {
        super(name, tab, material);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            boolean acceptFluid = FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing);
            if (!acceptFluid && te instanceof TileEntityBarrel)
                playerIn.sendMessage(new TextComponentString(((TileEntityBarrel) te).GetChatQuantity()));
            else if (!acceptFluid) doAdditionalFunction(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey("FluidName") && nbt.hasKey("Amount"))
        {

            tooltip.add(nbt.getString("FluidName") + ": " + nbt.getInteger("Amount"));
        }
    }

    public void doAdditionalFunction(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityPortableGenerator && stack.getTagCompound() != null && stack.getTagCompound().hasKey("FluidName"))
        {
            ((TileEntityPortableGenerator) te).getTank().readFromNBT(stack.getTagCompound());
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntitySaveContent)
        {
            ItemStack itemst = SaveStackContainer((TileEntitySaveContent) te);
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        return te instanceof TileEntitySaveContent ? (int) (Utils.normalize(((TileEntitySaveContent) te).getTank().getFluidAmount(), 0, ((TileEntityPortableGenerator) te).getTank().getCapacity()) * 15) : 0;
    }

    private ItemStack SaveStackContainer(TileEntitySaveContent te)
    {
        ItemStack stack = new ItemStack(getItemToDrop());
        if (te != null)
        {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) nbt = new NBTTagCompound();
            if (te.getTank().getFluid() != null)
            {
                te.getTank().writeToNBT(nbt);
                stack.setTagCompound(nbt);
            }
        }
        return stack;
    }

    public abstract Item getItemToDrop();

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntitySaveContent te = (TileEntitySaveContent) world.getTileEntity(pos);
        return SaveStackContainer(te);
    }
}
