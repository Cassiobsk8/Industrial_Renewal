package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

public class BlockRazorWire extends BlockHorizontalFacing
{
    public static final PropertyBool FRAME = PropertyBool.create("frame");

    public BlockRazorWire(String name, CreativeTabs tab)
    {
        super(name, tab, Material.WEB);
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            entityIn.setInWeb();
            entityIn.attackEntityFrom(DamageSource.CACTUS, IRConfig.MainConfig.Main.razorWireDamage);
        }
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
}
