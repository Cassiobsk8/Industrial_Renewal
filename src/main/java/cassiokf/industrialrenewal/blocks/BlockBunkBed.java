package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.tileentity.TileEntityBunkBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.minecraft.block.BlockBed.EnumPartType.FOOT;
import static net.minecraft.block.BlockBed.EnumPartType.HEAD;

public class BlockBunkBed extends BlockBed
{
    public static final PropertyBool TOP = PropertyBool.create("top");

    protected String name;

    public BlockBunkBed(String name, CreativeTabs tab)
    {
        this.name = name;

        setRegistryName(References.MODID, name);
        setTranslationKey(References.MODID + "." + name);
        setCreativeTab(tab);
        setHardness(1f);
        setResistance(5f);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos,  BlockState state)
    {
        if (state.getValue(PART) == FOOT)
        {
            worldIn.setBlockState(pos.offset(state.getValue(FACING)), state.withProperty(PART, HEAD));
        }
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos,  BlockState state)
    {
        switch (state.getValue(PART))
        {
            case FOOT:
                if (isBunkBed(worldIn, pos.offset(state.getValue(FACING))))
                    worldIn.setBlockToAir(pos.offset(state.getValue(FACING)));
                break;
            case HEAD:
                if (isBunkBed(worldIn, pos.offset(state.getValue(FACING).getOpposite())))
                    worldIn.setBlockToAir(pos.offset(state.getValue(FACING).getOpposite()));
                break;
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos,  BlockState state, TileEntity te, ItemStack stack)
    {
        ItemStack itemstack = this.getItem(worldIn, pos, state);
        spawnAsEntity(worldIn, pos, itemstack);
    }

    private boolean isBunkBed(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockBunkBed;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        PlayerEntity player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
                && worldIn.getBlockState(pos.offset(player.getHorizontalFacing())).getBlock().isReplaceable(worldIn, pos.offset(player.getHorizontalFacing()));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos,  BlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1);
    }

    @Override
    public  BlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        boolean top = state.getValue(PART) == FOOT && worldIn.getBlockState(pos.down()).getBlock() instanceof BlockBunkBed;
        return state.withProperty(TOP, top);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, PART, OCCUPIED, TOP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(PART, FOOT);
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

    @Nullable
    @Override
    public TileEntityBunkBed createTileEntity(World world,  BlockState state)
    {
        return new TileEntityBunkBed();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBunkBed();
    }

    public void registerItemModel(Item itemBlock)
    {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock()
    {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}
