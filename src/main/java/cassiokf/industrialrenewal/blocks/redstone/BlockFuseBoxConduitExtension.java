package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFuseBoxConduitExtension extends BlockBase
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0F, 0.375F, 0.125F, 1F, 0.625D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0F, 0.375F, 0.875F, 1F, 0.625D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.375F, 0F, 0.875F, 0.625D, 1F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.375F, 0F, 0.125F, 0.625D, 1F, 0);

    public BlockFuseBoxConduitExtension(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @SuppressWarnings("deprecation")
    @Override
    public  BlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public  BlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getActualState(source, pos).getValue(FACING)) {
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
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
