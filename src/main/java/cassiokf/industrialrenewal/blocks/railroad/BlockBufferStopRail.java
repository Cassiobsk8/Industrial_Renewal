package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBufferStopRail extends BlockHorizontalFacing
{
    public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;

    protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);

    public BlockBufferStopRail(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setHardness(0.8f);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (entityIn instanceof EntityMinecart)
        {
            entityIn.motionX = 0;
            entityIn.motionY = 0;
            entityIn.motionZ = 0;
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FLAT_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return FLAT_AABB;
    }
}