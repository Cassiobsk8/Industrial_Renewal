package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRazorWire extends BlockBase
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool FRAME = PropertyBool.create("frame");

    public BlockRazorWire(String name, CreativeTabs tab)
    {
        super(Material.WEB, name, tab);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {

        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        entityIn.setInWeb();
        entityIn.attackEntityFrom(DamageSource.CACTUS, IRConfig.MainConfig.Main.razorWireDammage);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, FRAME);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(FRAME, canConnect(worldIn, pos, state));
    }

    private boolean canConnect(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        EnumFacing facing = state.getValue(FACING);
        return !(world.getBlockState(pos.offset(facing.rotateY())).getBlock() instanceof BlockRazorWire);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
