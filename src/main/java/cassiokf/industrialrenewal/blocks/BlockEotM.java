package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEotM extends BlockHorizontalFacing
{
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0D, 0.0625D, 0.1875D, 0.0625D, 0.9375D, 0.8125D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1D, 0.0625D, 0.1875D, 0.9375D, 0.9375D, 0.8125D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.1875D, 0.0625D, 0.9375D, 0.8125D, 0.9375D, 1D);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.1875D, 0.0625D, 0.0625D, 0.8125D, 0.9375D, 0D);

    public BlockEotM(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing face = null;
        for (EnumFacing face1 : EnumFacing.HORIZONTALS)
        {
            if (facing.equals(face1))
            {
                face = facing.getOpposite();
            }
        }
        if (face == null) face = placer.getHorizontalFacing();

        return getDefaultState().withProperty(FACING, face);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getActualState(source, pos).getValue(FACING))
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

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
